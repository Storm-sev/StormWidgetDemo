package com.example.customnewdemo.app.anchortask

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.example.customnewdemo.BuildConfig
import com.example.customnewdemo.app.MyApplication
import com.example.customnewdemo.common.BaseConstant
import com.example.customnewdemo.net.NetWorkManager
import com.example.customnewdemo.utils.AppUtils
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsDownloader
import com.tencent.smtt.sdk.TbsListener
import com.xj.anchortask.library.AnchorTask
import com.xj.anchortask.library.IAnchorTaskCreator
import java.lang.Exception

class ApplicationAnchorTaskCreator() : IAnchorTaskCreator {

    override fun createTask(taskName: String): AnchorTask? {
        when (taskName) {
            TASK_ANCHOR_NETWORK -> {
                return NetWorkAnchorTask()
            }

            TBS_NAME_TASK -> {
                return TbsAnchorTask();
            }

            TASK_NAME_LOGCONFIG ->{
                return LOGUtilsAnchorTask()
            }
        }
        return null;
    }
}


val TASK_NAME_LOGCONFIG = "task_name_logconfig";

class LOGUtilsAnchorTask : AnchorTask(TASK_NAME_LOGCONFIG){
    companion object {
        val TAG = "LOGUtilsAnchorTask"
    }

    override fun isRunOnMainThread(): Boolean {
        return false
    }

    override fun run() {


        try{

            setLogConfig();

        }catch (e:Exception){

        }
    }


    private fun setLogConfig() {

        MyApplication.mLogConfig = com.example.customnewdemo.utils.LogUtils.getConfig()
            .setLogSwitch(true)
            .setConsoleSwitch(BuildConfig.DEBUG)
            .setGlobalTag("storm_log")
            .setLog2FileSwitch(false)
            .setSingleTagSwitch(true)
            .setLogHeadSwitch(true)
            .setBorderSwitch(true)
            .setConsoleFilter(com.example.customnewdemo.utils.LogUtils.V)
            .setFileFilter(com.example.customnewdemo.utils.LogUtils.V)
    }




}

var TASK_ANCHOR_NETWORK = "task_anchor_network"

class NetWorkAnchorTask() : AnchorTask(TASK_ANCHOR_NETWORK) {

    companion object {
        val TAG = "NetWorkAnchorTask"
    }


    override fun isRunOnMainThread(): Boolean {
        return true;
    }

    override fun run() {
        val start = System.currentTimeMillis();

        try {
            NetWorkManager.instance.init(AppUtils.appContext).baseUrl(BaseConstant.BASE_URL)
        } catch (e: Exception) {

        }
        LogUtils.d(TAG, "$ AnchorTaskNetWork --> ${System.currentTimeMillis() - start}")
    }
}


var TBS_NAME_TASK = "tbs_name_task"

class TbsAnchorTask : AnchorTask(TBS_NAME_TASK) {

    companion object {
        val TAG = "TbsAnchorTask"
    }

    override fun isRunOnMainThread(): Boolean {
        return false;
    }

    override fun run() {
        val start = System.currentTimeMillis();

        try {
            setUpTBS(MyApplication.appContext)
        } catch (e: Exception) {

        }

        LogUtils.d(
            NetWorkAnchorTask.TAG,
            "$ AnchorTaskNetWork --> ${System.currentTimeMillis() - start}"
        )
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
                com.example.customnewdemo.utils.LogUtils.d(MyApplication.TAG, "内核初始化成功")
            }

            override fun onDownloadProgress(p0: Int) {

                com.example.customnewdemo.utils.LogUtils.d(
                    MyApplication.TAG,
                    "获取加载进度 x5 ---> $p0"
                )
            }
        })


        val needDownload = TbsDownloader.needDownload(context, TbsDownloader.DOWNLOAD_OVERSEA_TBS)
        com.example.customnewdemo.utils.LogUtils.d(
            MyApplication.TAG,
            "是否需要下载内核--> $needDownload"
        )

        if (needDownload) {
            TbsDownloader.startDownload(context)

        }

        QbSdk.initX5Environment(context, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
                com.example.customnewdemo.utils.LogUtils.d(
                    MyApplication.TAG,
                    " 调用 --> onCoreInitFinished"
                )

            }

            override fun onViewInitFinished(p0: Boolean) {
                com.example.customnewdemo.utils.LogUtils.d(
                    MyApplication.TAG,
                    " 调用 --> onViewInitFinished -${p0}"
                )

            }
        })
    }


}