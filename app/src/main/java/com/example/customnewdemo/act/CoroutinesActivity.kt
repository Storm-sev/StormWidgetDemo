package com.example.customnewdemo.act

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customnewdemo.R
import com.example.customnewdemo.databinding.ActivityCoroutinesBinding

/**
 * 协程相关
 */
class CoroutinesActivity : AppCompatActivity() {

    private lateinit var binding  : ActivityCoroutinesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoroutinesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object{

        val TAG = "CoroutinesActivity"

        public fun startSelf(activity: Activity) {
            val intent = Intent(activity, CoroutinesActivity::class.java)
            activity.startActivity(intent)
        }
    }

}