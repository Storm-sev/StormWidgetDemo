package com.example.customnewdemo.act

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
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

        binding.tvLoadVideo.setOnClickListener {

            loadVideoPath()
        }
    }

    private fun loadVideoPath() {

        val  url = "http://ejz.yunchewen.com/ftp/auditorium/video/20211009/5626af2f021547399df874f8626bec6f.mp4"

        val options = RequestOptions()
        options.skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()

        Glide.with(this)
            .setDefaultRequestOptions(options)
            .load(url)
            .into(binding.ivVideoFirst)

    }


}