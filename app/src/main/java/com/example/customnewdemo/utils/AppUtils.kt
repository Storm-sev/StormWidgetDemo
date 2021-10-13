package com.example.customnewdemo.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.example.customnewdemo.app.MyApplication

object AppUtils {

    val appContext: Context
        get() = MyApplication.appContext


    fun startSystemSetting(context: Context) {
        val intent = Intent()
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.setData(Uri.fromParts("package", context.packageName, null))
        context.startActivity(intent)

    }
}