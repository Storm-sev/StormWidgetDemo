package com.example.customnewdemo.camera

import android.app.Activity
import android.content.Context
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.view.TextureView
import com.example.customnewdemo.utils.LogUtils

/**
 * 相关
 */

class Camera2Helper2(val context: Context, private val mTextureView: TextureView) {


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


    private val mPreviewSize: Size = Size(PREVIEW_WIDTH, PREVIEW_HEIGHT)
    private val mSaveSize: Size = Size(SAVE_WIDTH, SAVE_HEIGHT)

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


    }


}