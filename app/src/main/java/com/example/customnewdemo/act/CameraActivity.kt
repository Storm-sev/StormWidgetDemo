package com.example.customnewdemo.act

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customnewdemo.camera.CameraHelper
import com.example.customnewdemo.databinding.ActivityCameraBinding

 const val REQUEST_CODE = 1001;
class CameraActivity : AppCompatActivity() {

    companion object{
        val TAG = "CameraActivity"

        fun startSelf(activity: Activity) {
            val intent = Intent(activity, CameraActivity::class.java)
            activity.startActivity(intent)
        }
    }
    lateinit var binding: ActivityCameraBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpListener()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
        }
    }

    private fun setUpListener() {

        binding.tvTakePhoto.setOnClickListener {
            CameraHelper.instance.takePicture()
        }
    }

    override fun onResume() {
        super.onResume()
        CameraHelper.instance.init(this,binding.flCamera)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        CameraHelper.instance.onRequestPermissionResult(requestCode, permissions, grantResults)

    }


}