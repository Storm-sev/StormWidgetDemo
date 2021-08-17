package com.example.customnewdemo.camera

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.view.Surface
import android.view.TextureView
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
    private lateinit var mCameraCharacteristics: CameraCharacteristics // 用于描述摄像头的各种特性,

    private lateinit var mCameraManager: CameraManager
    private var mCameraSensorOrientation = 0  // 摄像头方向  初始为 0
    private var mDisplayRotation = context.windowManager.defaultDisplay.rotation // 获取手机的方向

    private var mCameraDevice: CameraDevice? = null

    init {
        handleThread.start()
        mCameraHandler = Handler(handleThread.looper)

        // 预览监听
        mTextureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                initCamera()
            }

            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {
            }

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {

                return false
            }


            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
            }
        }


    }


    private var mPreviewSize: Size = Size(PREVIEW_WIDTH, PREVIEW_HEIGHT)
    private var mSaveSize: Size = Size(SAVE_WIDTH, SAVE_HEIGHT)
    private var mImageReader: ImageReader? = null

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

        val previewSize = configurationMap?.getOutputSizes(ImageFormat.JPEG)
        val saveSize = configurationMap?.getOutputSizes(SurfaceTexture::class.java)

        val exchange =
            exchangeWidthAndHeight(mDisplayRotation, mCameraSensorOrientation)

        // 获取保存的尺寸
        mSaveSize = getBestSize(
            if (exchange) mSaveSize.height else mSaveSize.width,
            if (exchange) mSaveSize.width else mSaveSize.height,
            if (exchange) mSaveSize.height else mSaveSize.width,
            if (exchange) mSaveSize.width else mSaveSize.height,
            saveSize?.toList() ?: emptyList()
        )

        // 获取 预览尺寸
        mPreviewSize = getBestSize(
            if (exchange) mPreviewSize.height else mPreviewSize.width,
            if (exchange) mPreviewSize.width else mPreviewSize.height,
            if (exchange) mPreviewSize.height else mPreviewSize.width,
            if (exchange) mPreviewSize.width else mPreviewSize.height,
            previewSize?.toList() ?: emptyList()
        )


        mTextureView.surfaceTexture?.setDefaultBufferSize(mPreviewSize.width, mPreviewSize.height)
        LogUtils.d(
            TAG,
            "预览最优尺寸 ：${mPreviewSize.width} * ${mPreviewSize.height}, 比例  ${mPreviewSize.width.toFloat() / mPreviewSize.height}"
        )
        LogUtils.d(
            TAG,
            "保存图片最优尺寸 ：${mSaveSize.width} * ${mSaveSize.height}, 比例  ${mSaveSize.width.toFloat() / mSaveSize.height}"
        )

        // 设置 reader

        mImageReader =
            ImageReader.newInstance(mSaveSize.width, mSaveSize.height, ImageFormat.JPEG, 1)

        mImageReader?.setOnImageAvailableListener(onImageAvailableListener, mCameraHandler)

        openCamera()
    }


    private val onImageAvailableListener = ImageReader.OnImageAvailableListener {
        // 处理得到的照片
    }


    /**
     * 开启相机
     */
    private fun openCamera() {
        // 权限检查
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED

        ) {
            LogUtils.d(TAG, "没有相机权限")
// 权限交给外层去申请 不在这里申请 否则 有其他跳转逻辑
            return

        }

        mCameraManager.openCamera(mCameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(cameraDevice: CameraDevice) {

                mCameraDevice= cameraDevice
                // 创建预览会话

            }

            override fun onDisconnected(p0: CameraDevice) {

            }

            override fun onError(p0: CameraDevice, p1: Int) {

            }
        }, mCameraHandler)

    }

    /**
     * 根据提供的指向[displayRotation] 和 [sensorOrientation] 返回是否需要交换宽高
     */
    public fun exchangeWidthAndHeight(displayRotation: Int, sensorOrientation: Int): Boolean {

        var exchange = false
        when (displayRotation) {
            Surface.ROTATION_0, Surface.ROTATION_270 -> {
                if (sensorOrientation == 90 || sensorOrientation == 270) { // 横屏
                    exchange = true
                }
            }
            Surface.ROTATION_90, Surface.ROTATION_180 -> {
                if (sensorOrientation == 0 || sensorOrientation == 180) {// 竖直方向
                    exchange = true
                }
            }
            else -> LogUtils.d("displayRotation  is  $displayRotation")
        }

        LogUtils.d(TAG, "屏幕方向 -> $displayRotation")
        LogUtils.d(TAG, "相机方向 -> $sensorOrientation")
        return exchange
    }


    /**
     * 根据提供的参数值返回与指定宽高相等或最接近的尺寸
     */
    private fun getBestSize(
        targetWidth: Int, targetHeight: Int,
        maxWidth: Int, maxHeight: Int, sizeList: List<Size>
    ): Size {

        val bigEnough = ArrayList<Size>() // 比指定宽高大的size 列表

        val notBigEnough = arrayListOf<Size>() // 比指定宽高小的size 列表

        for (size in sizeList) {
            if (size.width <= maxWidth && size.height <= maxHeight
                && size.width == size.height * targetWidth / targetHeight
            ) {
                if (size.width >= targetWidth && size.height >= targetHeight) {
                    bigEnough.add(size)
                } else {
                    notBigEnough.add(size)
                }
            }
        }

        return when {
            bigEnough.size > 0 -> Collections.min(bigEnough, CompareSizesByArea())
            notBigEnough.size > 0 -> Collections.max(notBigEnough, CompareSizesByArea())
            else -> sizeList[0]

        }

    }


    /**
     * 比较规则
     */
    private class CompareSizesByArea : Comparator<Size> {
        override fun compare(size1: Size, size2: Size): Int {
            return java.lang.Long.signum(size1.width.toLong() * size1.height - size2.width.toLong() * size2.height)
        }
    }

}