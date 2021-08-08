package com.example.customnewdemo.app

import android.app.Application
import android.content.Context
import android.content.Intent
import com.example.customnewdemo.common.BaseConstant
import com.example.customnewdemo.net.NetWorkManager

class MyApplication : Application() {
    companion object {
        fun startLogin() {
            var intent = Intent("LOGIN")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            appContext.startActivity(intent)

        }

        lateinit var appContext: Context

        val TAG = "Application-->"
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        NetWorkManager.instance.init(this).baseUrl(BaseConstant.BASE_URL)
    }
}