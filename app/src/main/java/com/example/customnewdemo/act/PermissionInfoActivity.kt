package com.example.customnewdemo.act

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customnewdemo.R
import com.example.customnewdemo.databinding.ActivityPermissionInfoBinding
import com.example.customnewdemo.utils.AppUtils

class PermissionInfoActivity : AppCompatActivity() {

    companion object {
        public fun startSelf(activity: AppCompatActivity) {
            val intent = Intent(activity, PermissionInfoActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityPermissionInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpListener()
    }

    private fun setUpListener() {

        binding.tvStartSystem.setOnClickListener {
            AppUtils.startSystemSetting(this)

        }


    }


}