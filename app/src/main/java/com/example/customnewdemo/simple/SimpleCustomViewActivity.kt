package com.example.customnewdemo.simple

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customnewdemo.R

class SimpleCustomViewActivity : AppCompatActivity() {

    companion object{

        public fun startSelf(activity: Activity) {
            val intent = Intent(activity, SimpleCustomViewActivity::class.java)

            activity.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_custom_view)
    }



}