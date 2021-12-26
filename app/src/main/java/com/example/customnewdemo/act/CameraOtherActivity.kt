package com.example.customnewdemo.act

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.bumptech.glide.load.model.GlideUrl
import com.example.customnewdemo.R

import com.example.customnewdemo.databinding.ActivityCameraOtherBinding
import com.example.customnewdemo.databinding.LayoutCameraViewBinding
import com.example.customnewdemo.utils.GlideUtils
import com.example.customnewdemo.utils.LogUtils
import com.example.customnewdemo.utils.dip2px
import com.example.customnewdemo.widget.CustomCameraView
import com.tbruyelle.rxpermissions3.Permission
import com.tbruyelle.rxpermissions3.RxPermissions
import me.shouheng.icamera.CameraView

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
    lateinit var viewBinding : LayoutCameraViewBinding;
    lateinit var cameraview:CustomCameraView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraOtherBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        viewBinding = LayoutCameraViewBinding.inflate(LayoutInflater.from(this), null, true)
        cameraview = LayoutInflater.from(this).inflate(R.layout.layout_camera_view, null) as CustomCameraView
        viewBinding = LayoutCameraViewBinding.bind(cameraview)
        rxPermission = RxPermissions(this)
        initView()
        setUplisttener();

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            binding.rlRoot.removeView(cameraview)


            val fullLayout: ViewGroup.LayoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )

            val vg = window.decorView as ViewGroup
            vg.addView(cameraview, fullLayout)


        } else {

        }
    }

    lateinit var rxPermission: RxPermissions

    var lastX : Int = 0;
    var lastY :Int = 0;
    @SuppressLint("ClickableViewAccessibility")
    private fun setUplisttener() {


        viewBinding.flContent.setOnTouchListener { v, event ->
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

        viewBinding.cv.setCameraPreviewListener(object : CameraPreviewListener {
            override fun onPreviewFrame(data: ByteArray, size: Size, format: Int) {


            }
        })


    }
    var video_url = "https://stream7.iqilu.com/10339/upload_transcode/202002/18/20200218114723HDu3hhxqIT.mp4"
    var image_url = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2F2%2Fd4%2F39b7761410.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1642952058&t=6a7b945bbd8b5180b42cea4a90d1b252"

    private fun initView() {

        setUpCameraView()
        setUpVideo();



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

    private fun setUpCameraView() {
        var params = RelativeLayout.LayoutParams(
            100f.dip2px(), 130f.dip2px()
        )

        binding.rlRoot.addView(cameraview,params)


    }

    private fun setUpVideo() {
        binding.jzVideo.setUp(video_url,"测试视频")

        GlideUtils.loadImage(this,image_url,binding.jzVideo.posterImageView)


    }



    private fun openCamera() {

        viewBinding.cv.setUseTouchFocus(false)
        viewBinding.cv.switchCamera(CameraFace.FACE_FRONT)
        viewBinding.cv.setTouchZoomEnable(false)

        viewBinding.cv.openCamera(object : CameraOpenListener {

            override fun onCameraOpenError(throwable: Throwable) {

                LogUtils.d(TAG, "相机打开错误")

            }

            override fun onCameraOpened(cameraFace: Int) {
                LogUtils.d(TAG, "相机已经打开 ")

            }
        })

    }


}