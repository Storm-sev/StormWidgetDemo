package com.example.customnewdemo.app

import android.app.Application
import android.content.Context
import android.content.Intent

import com.example.customnewdemo.BuildConfig
import com.example.customnewdemo.app.anchortask.ApplicationAnchorTaskCreator
import com.example.customnewdemo.app.anchortask.TASK_ANCHOR_NETWORK
import com.example.customnewdemo.app.anchortask.TASK_NAME_LOGCONFIG

import com.example.customnewdemo.app.anchortask.TBS_NAME_TASK

import com.example.customnewdemo.utils.LogUtils

import com.xj.anchortask.library.AnchorProject


class MyApplication : Application() {

    companion object {
        var mLogConfig: LogUtils.Config? = null

        fun startLogin() {
            var intent = Intent("STORM_LOGIN")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            appContext.startActivity(intent)

        }

        lateinit var appContext: Context

        val TAG = "Application-->"
    }


    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext


//        val systemService = getSystemService("android.os.ServiceManager")
//
//
//        LogUtils.d(TAG, systemService.toString())
        loadSdk();

    }

    private fun loadSdk() {

        val project = AnchorProject
            .Builder().setContext(this)
            .setLogLevel(com.xj.anchortask.library.log.LogUtils.LogLevel.DEBUG)
            .setAnchorTaskCreator(ApplicationAnchorTaskCreator())
            .addTask(TASK_NAME_LOGCONFIG)
            .addTask(TASK_ANCHOR_NETWORK)
            .addTask(TBS_NAME_TASK).afterTask(TASK_NAME_LOGCONFIG, TASK_ANCHOR_NETWORK)
            .build()
        project.start().await()

    }


}