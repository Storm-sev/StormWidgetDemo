package com.example.customnewdemo.camera

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.lang.Exception
import android.hardware.camera2.CameraAccessException

import android.hardware.camera2.CaptureRequest

import android.hardware.camera2.CameraDevice
import android.util.Log
import android.view.Surface
import android.util.SparseIntArray
import android.view.ViewGroup
import com.example.customnewdemo.utils.BitmapUtils


/**
 * 拍照相关
 */
class CameraHelper {

    private var mContext: Context? = null
    private var surViewRoot: FrameLayout? = null
    private var mainHandler: Handler? = null
    private var childHandler: Handler? = null
    private var mCameraId: String = ""
    private var mCameraManager: CameraManager? = null


    init {
        ORIENTATIONS.append(Surface.ROTATION_0, 90)
        ORIENTATIONS.append(Surface.ROTATION_90, 0)
        ORIENTATIONS.append(Surface.ROTATION_180, 270)
        ORIENTATIONS.append(Surface.ROTATION_270, 180)
    }

    private constructor() {


    }


    companion object {
        private val ORIENTATIONS = SparseIntArray()

        val REQUEST_CAMERA_CODE: Int = 1001

        val TAG = "CameraHelper"

        // 单例
        val instance: CameraHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CameraHelper()
        }
    }


    public fun init(context: Context, frameLayout: FrameLayout) {
        this.mContext = context
        this.surViewRoot = frameLayout
        mSurfaceView = SurfaceView(mContext)

        setUpSurView()
    }

    private var mCameraDevice: CameraDevice? = null // 摄像

    private var mSurfaceView: SurfaceView? = null
    private var mSurFaceHolder: SurfaceHolder? = null
    private var stateCallBack: CameraDevice.StateCallback =
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        object : CameraDevice.StateCallback() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onOpened(camera: CameraDevice) {

                mCameraDevice = camera
                // 开启预览;
                takePreView()


            }

            override fun onDisconnected(camera: CameraDevice) {
                if (null != mCameraDevice) {
                    mCameraDevice!!.close()
                    mCameraDevice = null

                }
            }

            override fun onError(camera: CameraDevice, error: Int) {


            }

        }

    private var mCameraCaptureSession: CameraCaptureSession? = null // 相册预览现骨干

    /**
     * 开启预览功能
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun takePreView() {
        try {

            // 创建预览需要的CaptureRequest.Builder
            val previewRequestBuilder =
                mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            //将surfceVIew 的surface 作为目标
            previewRequestBuilder.addTarget(mSurFaceHolder!!.surface)
            // 创建 cameraCaptureSession 作为处理和拍照请求
            mCameraDevice?.createCaptureSession(
                arrayListOf(mSurFaceHolder!!.surface, mImageReader!!.surface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        if (null == mCameraDevice) {
                            return
                        }

                        mCameraCaptureSession = session
                        try {
                            // 自动对焦
                            previewRequestBuilder.set(
                                CaptureRequest.CONTROL_AF_MODE,
                                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                            )
                            // 打开闪光灯
//                            previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

                            // 显示预览

                            var build = previewRequestBuilder.build()
                            mCameraCaptureSession!!.setRepeatingRequest(build, null, childHandler)
                        } catch (e: CameraAccessException) {
                            e.printStackTrace()
                        }

                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        TODO("Not yet implemented")
                    }
                },
                childHandler
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    public fun setSurfaceRootView(frameLayout: FrameLayout) {
        surViewRoot = frameLayout
    }

    /**
     * 初始化相机
     */
    fun setUpSurView() {

        if (null == surViewRoot) {
            return
        }


        mSurfaceView?.let {

            surViewRoot!!.removeAllViews()
            var params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )

            surViewRoot!!.addView(it, params)
            mSurFaceHolder = it.holder
            mSurFaceHolder?.setKeepScreenOn(true)



            mSurFaceHolder?.addCallback(object : SurfaceHolder.Callback {
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun surfaceCreated(holder: SurfaceHolder) {
                    // 创建 之后 初始化相机
                    initCamera2()
                }

                override fun surfaceChanged(
                    holder: SurfaceHolder,

                    format: Int,
                    width: Int,
                    height: Int
                ) {

                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {
                    if (null != mCameraDevice) {
                        mCameraDevice!!.close()
                        mCameraDevice = null
                    }
                }

            })
        }
    }

    private var mImageReader: ImageReader? = null
    private var image: Image? = null
    private var mCameraSensorOrientation: Int = 0;

    // 初始化相机
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun initCamera2() {
        mCameraManager = mContext!!.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        val cameraIdList = mCameraManager!!.cameraIdList
        if (cameraIdList.isEmpty()) {
            //没有可用相机
            return
        }

        for (id in cameraIdList) {
            val cameraCharacteristics = mCameraManager!!.getCameraCharacteristics(id)

            val face = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)
            if (face == CameraCharacteristics.LENS_FACING_BACK) {
//                mCameraSensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)
                mCameraSensorOrientation =
                    cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) as Int

                Log.d(TAG, "获取的相机传感器的自然方向 " + mCameraSensorOrientation)
            }

        }

        val handleThread = HandlerThread("camera2")
        handleThread.start()
        childHandler = Handler(handleThread.looper)
        mainHandler = Handler(Looper.getMainLooper())
        mCameraId = "" + CameraCharacteristics.LENS_FACING_BACK

        mImageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 1)

        mImageReader?.setOnImageAvailableListener(
            ImageReader.OnImageAvailableListener { imgReader ->
                //处理得到的照片

                image = imgReader.acquireNextImage()



                image?.let {
                    var buffer = it.planes[0].buffer
                    var bytes = ByteArray(buffer.remaining())

                    takePhotoCallBack?.savePic(bytes)
                    buffer.get(bytes)
                    var bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                    takePhotoCallBack?.getPhoto(bitmap)
                    image?.close()

                    BitmapUtils.savePic(bytes,false,{savedPath, time ->

                        Log.d(TAG,"保存成功")
                    },{msg ->
                        Log.d(TAG,"保存失败")

                    })
                }

            },
            mainHandler
        )



        try {
            if (ActivityCompat.checkSelfPermission(mContext!!, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                val strs = arrayOf<String>(android.Manifest.permission.CAMERA)
                val activity = mContext as Activity

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.requestPermissions(
                        strs,
                        REQUEST_CAMERA_CODE
                    )
                }

            } else {
                // 打开相机
                mCameraManager?.openCamera(mCameraId, stateCallBack, mainHandler)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun onRequestPermissionResult(
        requestCode: Int,
        @NonNull permissions: Array<out String>, @NonNull grantResult: IntArray
    ) {
        if (requestCode == REQUEST_CAMERA_CODE) {
            //
            if (grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                try {

                    mCameraManager?.openCamera(mCameraId, stateCallBack, mainHandler)
                } catch (e: CameraAccessException) {
                    e.printStackTrace()
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }
        } else {
            Toast.makeText(mContext, "未获取到权限", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun takePicture() {
        if (mCameraDevice == null) return
        //创建拍照所需要的CaptureRequestBuilder
        val captureResquestBuilder: CaptureRequest.Builder
        try {
            captureResquestBuilder =
                mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            //将imageReader的surface作为CaptureRequest.Builder的目标
            captureResquestBuilder.addTarget(mImageReader!!.surface)
            //自动对焦
            captureResquestBuilder.set(
                CaptureRequest.CONTROL_AF_MODE,
                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
            )
            //自动曝光
            captureResquestBuilder.set(
                CaptureRequest.CONTROL_AE_MODE,
                CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
            )
            //获取手机的自然方向  0 竖屏  1 横屏
            val rotation: Int =
                (mContext as Activity).getWindowManager().getDefaultDisplay().getRotation()
            Log.d(TAG, "获取手机的自然方向  -->  " + rotation)
            //根据设备方向计算设置照片的方向
            captureResquestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS[2])
            //拍照
            val mCaptureRequest = captureResquestBuilder.build()
            mCameraCaptureSession!!.capture(mCaptureRequest, null, childHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }


    private var takePhotoCallBack: TakePhotoCallBack? = null

    public fun setTakePhotoCallBack(takePhotoCallBack: TakePhotoCallBack) {
        this.takePhotoCallBack = takePhotoCallBack
    }

    interface TakePhotoCallBack {
        fun getPhoto(bitmap: Bitmap)

        fun savePic(bytes: ByteArray)

    }
}