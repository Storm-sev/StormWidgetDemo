package com.example.customnewdemo.act

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.RelativeLayout
import com.example.customnewdemo.R
import com.example.customnewdemo.databinding.ActivityLoadPdfBinding
import com.example.customnewdemo.net.NetWorkManager
import com.example.customnewdemo.utils.LogUtils

import com.liulishuo.okdownload.DownloadListener
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.tbruyelle.rxpermissions3.RxPermissions
import com.tencent.smtt.sdk.TbsReaderView
import io.reactivex.rxjava3.functions.Consumer
import java.io.File
import java.lang.Exception
import android.text.TextUtils
import android.util.Log


class LoadPdfActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoadPdfBinding


    private var fileUrl: String = "http://pop.yunchewen.com/ftp/pop/other/14712021121510540411.pdf"
    private var tbsFilePath = ""
    private lateinit var tbsReadView: TbsReaderView

    lateinit var rxPermissions: RxPermissions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rxPermissions = RxPermissions(this)

        tbsReadView = TbsReaderView(this , object : TbsReaderView.ReaderCallback{
            override fun onCallBackAction(p0: Int?, p1: Any?, p2: Any?) {

                LogUtils.d(TAG, "p0 -->  $p0 + p1 -->  $p1   p2 --->  $p2")



            }
        })




        binding.rlRoot.addView(
            tbsReadView, RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT

            )
        )

        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe(Consumer {
                if (it) {
                    loadPdf();
                }
            })



    }

    private var files : File? =null

    private fun loadPdf() {
        var tbsFilePath = getExternalFilesDir(null)

        files  = File(tbsFilePath!!.path + "/TbsReaderTemp")

        if (!files!!.exists())
            files!!.mkdirs()

        LogUtils.d(TAG, "获取的文件存储地址 --> ${tbsFilePath!!.path} -- 文件是否存在 -- > ${files!!.exists()}")
//        val fileName = getFileName(fileUrl)
//
//        LogUtils.d(TAG, "文件名字 --> $fileName")


        var task = DownloadTask.Builder(fileUrl,files!!)
            .setFilename(getFileName(fileUrl))
            // the minimal interval millisecond for callback progress
            .setMinIntervalMillisCallbackProcess(30)
            // do re-download even if the task has already been completed in the past.
            .setPassIfAlreadyCompleted(true)
            .build()

        task.enqueue(object : DownloadListener{
            override fun taskStart(task: DownloadTask) {

            }

            override fun connectTrialStart(
                task: DownloadTask,
                requestHeaderFields: MutableMap<String, MutableList<String>>
            ) {

            }

            override fun connectTrialEnd(
                task: DownloadTask,
                responseCode: Int,
                responseHeaderFields: MutableMap<String, MutableList<String>>
            ) {

            }

            override fun downloadFromBeginning(
                task: DownloadTask,
                info: BreakpointInfo,
                cause: ResumeFailedCause
            ) {

            }

            override fun downloadFromBreakpoint(task: DownloadTask, info: BreakpointInfo) {

            }

            override fun connectStart(
                task: DownloadTask,
                blockIndex: Int,
                requestHeaderFields: MutableMap<String, MutableList<String>>
            ) {

            }

            override fun connectEnd(
                task: DownloadTask,
                blockIndex: Int,
                responseCode: Int,
                responseHeaderFields: MutableMap<String, MutableList<String>>
            ) {

            }

            override fun fetchStart(task: DownloadTask, blockIndex: Int, contentLength: Long) {

            }

            override fun fetchProgress(task: DownloadTask, blockIndex: Int, increaseBytes: Long) {

            }

            override fun fetchEnd(task: DownloadTask, blockIndex: Int, contentLength: Long) {

            }

            override fun taskEnd(task: DownloadTask, cause: EndCause, realCause: Exception?) {

                LogUtils.d(TAG,"文件下载完成 ")

                displayPdf(File(files!!.path, getFileName(fileUrl)))

            }
        })








    }



    private fun displayPdf(file: File) {
        if (file.exists()) {
            LogUtils.d(TAG,"下载文件存在 -> ${file.absolutePath}")
        }

        var bundle = Bundle()
        bundle.putString("filePath",file.absolutePath);
        bundle.putString("tempPath",files!!.absolutePath)
        var preOpen = tbsReadView.preOpen(getFileType(getFileName(fileUrl)), false)
        LogUtils.d(TAG, "是否可以打开 --> $preOpen")

        if (preOpen) {
            tbsReadView.openFile(bundle)

        }

    }

    private fun getFileName(fileUrl: String): String {
       return  fileUrl.substring(fileUrl.lastIndexOf("/")+1)
    }

    companion object{

        val TAG = "LoadPdfActivity"
        public fun startSlef(activity: Activity) {
            val intent = Intent(activity, LoadPdfActivity::class.java)

            activity.startActivity(intent)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        tbsReadView.onStop()
    }

    private fun getFileType(paramString: String): String? {
        var str = ""
        if (TextUtils.isEmpty(paramString)) {
            Log.d("print", "paramString---->null")
            return str
        }
        Log.d("print", "paramString:$paramString")
        val i = paramString.lastIndexOf('.')
        if (i <= -1) {
            Log.d("print", "i <= -1")
            return str
        }
        str = paramString.substring(i + 1)
        Log.d("print", "paramString.substring(i + 1)------>$str")
        return str
    }


}