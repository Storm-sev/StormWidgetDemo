package com.example.customnewdemo.app

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.customnewdemo.BuildConfig
import com.example.customnewdemo.common.BaseConstant
import com.example.customnewdemo.net.NetWorkManager
import com.example.customnewdemo.utils.LogUtils
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsDownloader
import com.tencent.smtt.sdk.TbsListener


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


    public fun setUpTBS(context: Context) {
        val map = HashMap<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)
        QbSdk.setDownloadWithoutWifi(true)
//        val map = HashMap<String, Any>()
//        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
//        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
//        QbSdk.initTbsSettings(map)

        QbSdk.setTbsListener(object : TbsListener {
            override fun onDownloadFinish(p0: Int) {

            }

            override fun onInstallFinish(p0: Int) {
                LogUtils.d(TAG, "内核初始化成功")
            }

            override fun onDownloadProgress(p0: Int) {

                LogUtils.d(TAG, "获取加载进度 x5 ---> $p0")
            }
        })


        val needDownload = TbsDownloader.needDownload(context, TbsDownloader.DOWNLOAD_OVERSEA_TBS)
        LogUtils.d(TAG, "是否需要下载内核--> $needDownload")

        if (needDownload) {
            TbsDownloader.startDownload(context)

        }

        QbSdk.initX5Environment(context, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
                LogUtils.d(TAG, " 调用 --> onCoreInitFinished")

            }

            override fun onViewInitFinished(p0: Boolean) {
                LogUtils.d(TAG, " 调用 --> onViewInitFinished -${p0}")

            }
        })


    }


    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        setLogConfig()
        NetWorkManager.instance.init(this).baseUrl(BaseConstant.BASE_URL)

        setUpTBS(this)


//        val systemService = getSystemService("android.os.ServiceManager")
//
//
//        LogUtils.d(TAG, systemService.toString())


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