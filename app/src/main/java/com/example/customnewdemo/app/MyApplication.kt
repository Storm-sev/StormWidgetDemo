package com.example.customnewdemo.app

import android.app.Application
import android.content.Context
import android.content.Intent
import com.example.customnewdemo.BuildConfig
import com.example.customnewdemo.common.BaseConstant
import com.example.customnewdemo.net.NetWorkManager
import com.example.customnewdemo.utils.LogUtils

class MyApplication : Application() {

    companion object {
        var  mLogConfig : LogUtils.Config? = null

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
        setLogConfig()
        NetWorkManager.instance.init(this).baseUrl(BaseConstant.BASE_URL)
    }

    private fun setLogConfig() {

        mLogConfig = LogUtils.getConfig()
            .setLogSwitch(true)
            .setConsoleSwitch(BuildConfig.DEBUG)
            .setGlobalTag("storm_log")
            .setLog2FileSwitch(false)
            .setSingleTagSwitch(true)
            .setLogHeadSwitch(true)
            .setBorderSwitch(true)
            .setConsoleFilter(LogUtils.V)
            .setFileFilter(LogUtils.V)
    }
}