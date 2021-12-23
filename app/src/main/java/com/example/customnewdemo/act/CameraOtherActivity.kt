package com.example.customnewdemo.act

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View

import com.example.customnewdemo.databinding.ActivityCameraOtherBinding
import com.example.customnewdemo.utils.LogUtils
import com.tbruyelle.rxpermissions3.Permission
import com.tbruyelle.rxpermissions3.RxPermissions

import me.shouheng.icamera.config.ConfigurationProvider
import me.shouheng.icamera.config.creator.impl.Camera1OnlyCreator
import me.shouheng.icamera.config.creator.impl.CameraManagerCreatorImpl
import me.shouheng.icamera.config.creator.impl.CameraPreviewCreatorImpl
import me.shouheng.icamera.config.creator.impl.SurfaceViewOnlyCreator
import me.shouheng.icamera.config.size.Size
import me.shouheng.icamera.enums.CameraFace
import me.shouheng.icamera.listener.CameraOpenListener
import me.shouheng.icamera.listener.CameraPreviewListener
import me.shouheng.icamera.util.CameraHelper
import java.security.Permissions


class CameraOtherActivity : AppCompatActivity() {

    companion object {
        val TAG = "CameraOtherActivity"

        fun startSelf(activity: Activity) {
            val intent = Intent(activity, CameraOtherActivity::class.java)
            activity.startActivity(intent)

        }
    }


    lateinit var binding: ActivityCameraOtherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraOtherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rxPermission = RxPermissions(this)

        initView()
        setUplisttener();

    }

    lateinit var rxPermission: RxPermissions

    var lastX : Int = 0;
    var lastY :Int = 0;
    @SuppressLint("ClickableViewAccessibility")
    private fun setUplisttener() {


        binding.flContent.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.rawX.toInt()
                    lastY = event.rawY.toInt()

                }
                MotionEvent.ACTION_MOVE -> {
                    var dx = event.rawX - lastX
                    var dy = event.rawY - lastY


                    var left = v.left + dx
                    var top = v.top + dy
                    var right = left + v.width
                    var bottom = top + v.height

                    v.layout(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())


                    lastX = event.rawX.toInt()
                    lastY = event.rawY.toInt()

                }
                MotionEvent.ACTION_UP -> {

                }
            }
            true
        }

        binding.cv.setCameraPreviewListener(object : CameraPreviewListener {
            override fun onPreviewFrame(data: ByteArray, size: Size, format: Int) {


            }
        })


    }

    private fun initView() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConfigurationProvider.get().prepareCamera2(this)

        }



        rxPermission
            .request(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .subscribe { granted ->
                if (granted) { // Always true pre-M

                    openCamera();
                } else {
                    // Oups permission denied
                }
            }


    }

    private fun openCamera() {

        binding.cv.setUseTouchFocus(false)
        binding.cv.switchCamera(CameraFace.FACE_FRONT)
        binding.cv.setTouchZoomEnable(false)

        binding.cv.openCamera(object : CameraOpenListener {

            override fun onCameraOpenError(throwable: Throwable) {

                LogUtils.d(TAG, "相机打开错误")

            }

            override fun onCameraOpened(cameraFace: Int) {
                LogUtils.d(TAG, "相机已经打开 ")

            }
        })

    }
}