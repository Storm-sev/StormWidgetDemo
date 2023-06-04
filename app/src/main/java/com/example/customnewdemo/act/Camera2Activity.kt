package com.example.customnewdemo.act

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.customnewdemo.R
import com.example.customnewdemo.camera.Camera2Helper2
import com.example.customnewdemo.databinding.ActivityCamera2Binding
import com.example.customnewdemo.utils.LogUtils

/**
 * camera2 的时间
 */
class Camera2Activity : AppCompatActivity() {

    companion object {
        val TAG = "Camera2Activity"

        public fun startSelf(activity: Activity) {
            val intent = Intent(activity, Camera2Activity::class.java)
            activity.startActivity(intent)

        }
    }

    private var mCamera2Helper2: Camera2Helper2? = null

    private lateinit var binding: ActivityCamera2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCamera2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermission()
    }


    override fun onResume() {
        super.onResume()

        LogUtils.d(TAG, " onResume()")
//
//        if (mCamera2Helper2 == null) {
//            mCamera2Helper2 = Camera2Helper2(this, binding.tureViewPreview)
//
//        }

    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                requestPermissions(
                    arrayOf<String>(
                        Manifest.permission.CAMERA,

                        ), 1001
                )
            } else {
                // 开启相机
                mCamera2Helper2 = Camera2Helper2(this, binding.tureViewPreview)
                binding.tureViewPreview.visibility= View.VISIBLE


            }

        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {

            for (i in permissions.indices) {

                if (permissions[i] == Manifest.permission.CAMERA &&
                    grantResults[i] == PackageManager.PERMISSION_GRANTED

                ) {
                    LogUtils.d(TAG, "请求权限成功")
                    //   mCamera2Helper2 = Camera2Helper2(this, binding.tureViewPreview)

                    mCamera2Helper2 = Camera2Helper2(this, binding.tureViewPreview)
                    binding.tureViewPreview.visibility = View.VISIBLE
//                    binding.tureViewPreview.visibility = View.VISIBLE

                }

            }

        }

    }
}