package com.example.customnewdemo.camera

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.params.SessionConfiguration
import android.media.ImageReader
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.customnewdemo.utils.LogUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 * 相关
 */

class Camera2Helper2(val context: Activity, private val mTextureView: TextureView) {


    companion object {

        val TAG = "Camera2Helper2"
        const val PREVIEW_WIDTH = 720  // 预览的宽度
        const val PREVIEW_HEIGHT = 1280 // 预览的高度
        const val SAVE_WIDTH = 720 // 保存的宽度
        const val SAVE_HEIGHT = 1280 //保存的高度

    }

    private var mCameraHandler: Handler
    private var handleThread = HandlerThread("Camera2Thread")
    private var mCameraFaceing = CameraCharacteristics.LENS_FACING_BACK // 默认使用后置摄像头

    private var mCameraId = "0"
    private lateinit var mCameraCharacteristics: CameraCharacteristics

    private lateinit var mCameraManager: CameraManager
    private var mCameraSensorOrientation = 0  // 摄像头方向  初始为 0
    private var mDisplayRotation = context.windowManager.defaultDisplay.rotation // 获取手机的方向

    private var mImageReader: ImageReader? = null


    init {
        LogUtils.d(TAG, "初始化  init ---> ${mTextureView != null}")


        handleThread.start()
        mCameraHandler = Handler(handleThread.looper)

        // 预览监听
        mTextureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                LogUtils.d(TAG, "onSurfaceTextureAvailable")
                initCamera()
            }

            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {
                LogUtils.d(TAG, "onSurfaceTextureSizeChanged")

            }

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                LogUtils.d(TAG, "onSurfaceTextureDestroyed")
                // 销毁相机
                return true
            }


            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {

            }
        }


    }


    private var mPreviewSize: Size = Size(PREVIEW_WIDTH, PREVIEW_HEIGHT)
    private var mSaveSize: Size = Size(SAVE_WIDTH, SAVE_HEIGHT)

    /**
     * 初始化 camera
     */
    private fun initCamera() {
        // 获取cameramanager
        mCameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        //获取cameralist
        val cameraIdList = mCameraManager.cameraIdList
        if (cameraIdList.isEmpty()) {
            LogUtils.d(TAG, "没有可用的相机")
            // 此处需要加一个监听
            return
        }

        for (id in cameraIdList) {
            val cameraCharacteristics = mCameraManager.getCameraCharacteristics(id)
            val facing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)
            if (facing == mCameraFaceing) {
                mCameraId = id    // 当前 相机id
                mCameraCharacteristics = cameraCharacteristics  // 当前相机模式
            }
            LogUtils.d(TAG, "当前相机 id --> $id")

        }

        val supportLevel =
            mCameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
        if (supportLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
            // 相机不支持新特性
        }

        mCameraSensorOrientation =
            mCameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!

        //后去 scaleStreamConfigurationMap 管理所有摄像头输出的格式和尺寸
        val configurationMap =
            mCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

        val saveSize = configurationMap?.getOutputSizes(ImageFormat.JPEG)
        val previewSize = configurationMap?.getOutputSizes(SurfaceTexture::class.java)

        val exchange =
            exchangeWidthAndHeight(mDisplayRotation, mCameraSensorOrientation)

        // 图片保存的size
        mSaveSize = getBestSize(
            if (exchange) mSaveSize.height else mSaveSize.width,
            if (exchange) mSaveSize.width else mSaveSize.height,
            if (exchange) mSaveSize.height else mSaveSize.width,
            if (exchange) mSaveSize.width else mSaveSize.height,
            saveSize?.toList() ?: emptyList()
        )

        // 预览尺寸获取
        mPreviewSize = getBestSize(
            if (exchange) mPreviewSize.height else mPreviewSize.width,
            if (exchange) mPreviewSize.width else mPreviewSize.height,
            if (exchange) mPreviewSize.height else mPreviewSize.width,
            if (exchange) mPreviewSize.width else mPreviewSize.height,
            previewSize?.toList() ?: emptyList()
        )


        val surfaceTexture = mTextureView.surfaceTexture
        surfaceTexture?.setDefaultBufferSize(mPreviewSize.width, mPreviewSize.height)
//        try {
//            mTextureView.surfaceTexture?.setDefaultBufferSize(mPreviewSize.width, mPreviewSize.height)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

        LogUtils.d(
            TAG,
            "预览最优尺寸 ：${mPreviewSize.width} * ${mPreviewSize.height}, 比例  ${mPreviewSize.width.toFloat() / mPreviewSize.height}"
        )
        LogUtils.d(
            TAG,
            "保存图片最优尺寸 ：${mSaveSize.width} * ${mSaveSize.height}, 比例  ${mSaveSize.width.toFloat() / mSaveSize.height}"
        )


        mImageReader = ImageReader.newInstance(
            mPreviewSize.width, mPreviewSize.height,
            ImageFormat.JPEG, 1
        )

        mImageReader?.setOnImageAvailableListener(onImageAvailableListener, mCameraHandler)

        openCamera()

    }

    private var mCameraDevice: CameraDevice? = null
    private var mCameraCaptureSession: CameraCaptureSession? = null // 相机预览会话

    // 开启相机
    fun openCamera() {
        // 权限检查
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) !=
            PackageManager.PERMISSION_GRANTED
        ) {

            LogUtils.d(TAG, "没有权限 无法调用相机 ")
            return;
        }

        mCameraManager.openCamera(
            mCameraId,
            mStateCallBack,
            mCameraHandler

        )


    }


    private val mStateCallBack: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(cameraDevice: CameraDevice) {
            mCameraDevice = cameraDevice
            createCapturePreviewSession(cameraDevice)

        }

        override fun onDisconnected(p0: CameraDevice) {
        }

        override fun onError(p0: CameraDevice, p1: Int) {


        }
    }

    /**
     * 创建预览会话
     */

    private fun createCapturePreviewSession(cameraDevice: CameraDevice) {

        val captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        val surface = Surface(mTextureView.surfaceTexture)
        captureRequestBuilder.addTarget(surface) // 将 captureRequest 和 surface 绑定在一起
        captureRequestBuilder.set(
            CaptureRequest.CONTROL_AE_MODE,
            CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
        ) // 闪光灯
        captureRequestBuilder.set(
            CaptureRequest.CONTROL_AF_MODE,
            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
        ) // 自动对焦


//        cameraDevice.createCaptureSession(sessionCofig)
        // 创建预览的 cameraCaptureSession对象
        cameraDevice.createCaptureSession(
            arrayListOf(surface, mImageReader?.surface),
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {

                    LogUtils.d(TAG, "开启会话成功 ")

                    // 开启会话成功
                    mCameraCaptureSession = session
                    session.setRepeatingRequest(
                        captureRequestBuilder.build(),
                        mCaptureCallBack,
                        mCameraHandler
                    )


                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    LogUtils.d(TAG, "开启相机会话失败 ")


                }
            },
            mCameraHandler
        )

    }

    private var canExchangeCamera = false

    private var canTakePic = false

    public val mCaptureCallBack: CameraCaptureSession.CaptureCallback =
        object : CameraCaptureSession.CaptureCallback() {

            override fun onCaptureCompleted(
                session: CameraCaptureSession,
                request: CaptureRequest,
                result: TotalCaptureResult
            ) {
                super.onCaptureCompleted(session, request, result)

                //
                canExchangeCamera = true
                canTakePic = true


            }


            override fun onCaptureFailed(
                session: CameraCaptureSession,
                request: CaptureRequest,
                failure: CaptureFailure
            ) {
                super.onCaptureFailed(session, request, failure)
            }
        }

    public val onImageAvailableListener = ImageReader.OnImageAvailableListener {

    }

    /**
     * 根据提供的屏幕方向[displayRotation] 以及相机方向 [sensorOrientation] 返回是否需要交换宽高
     */
    public fun exchangeWidthAndHeight(displayRotation: Int, sensorOrientation: Int): Boolean {

        var exchagne = false
        when (displayRotation) {

            Surface.ROTATION_0, Surface.ROTATION_180 -> { // 屏幕方向上的 竖直方向
                if (sensorOrientation == 90 || sensorOrientation == 270) {
                    exchagne = true
                }

            }

            Surface.ROTATION_90, Surface.ROTATION_270 -> { // 屏幕方向  水平方向 .

                if (sensorOrientation == 0 && sensorOrientation == 180) {
                    exchagne = true
                }
            }
            else -> {
                LogUtils.d(
                    TAG, "displayRoataion --> $displayRotation"

                )
            }

        }

        LogUtils.d(TAG, "屏幕方向-> $displayRotation")
        LogUtils.d(TAG, "相机方向-> $sensorOrientation")

        return exchagne
    }


    private fun getBestSize(
        targetWidth: Int,
        targetHeight: Int,
        maxWidth: Int,
        maxHeight: Int,
        sizeList: List<Size>
    ): Size {
        val bigEnough = ArrayList<Size>()     //比指定宽高大的Size列表
        val notBigEnough = ArrayList<Size>()  //比指定宽高小的Size列表

        for (size in sizeList) {

            //宽<=最大宽度  &&  高<=最大高度  &&  宽高比 == 目标值宽高比
            if (size.width <= maxWidth && size.height <= maxHeight
                && size.width == size.height * targetWidth / targetHeight
            ) {

                if (size.width >= targetWidth && size.height >= targetHeight)
                    bigEnough.add(size)
                else
                    notBigEnough.add(size)
            }
            LogUtils.d(
                TAG,
                "系统支持的尺寸: ${size.width} * ${size.height} ,  比例 ：${size.width.toFloat() / size.height}"
            )
        }

        LogUtils.d(TAG, "最大尺寸 ：$maxWidth * $maxHeight, 比例 ：${targetWidth.toFloat() / targetHeight}")
        LogUtils.d(
            TAG,
            "目标尺寸 ：$targetWidth * $targetHeight, 比例 ：${targetWidth.toFloat() / targetHeight}"
        )

        //选择bigEnough中最小的值  或 notBigEnough中最大的值
        return when {
            bigEnough.size > 0 -> Collections.min(bigEnough, CompareSizesByArea())
            notBigEnough.size > 0 -> Collections.max(notBigEnough, CompareSizesByArea())
            else -> sizeList[0]
        }
    }

    private class CompareSizesByArea : Comparator<Size> {
        override fun compare(size1: Size, size2: Size): Int {
            return java.lang.Long.signum(size1.width.toLong() * size1.height - size2.width.toLong() * size2.height)
        }
    }

}