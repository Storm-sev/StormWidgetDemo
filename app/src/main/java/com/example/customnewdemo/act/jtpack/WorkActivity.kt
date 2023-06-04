package com.example.customnewdemo.act.jtpack

import android.net.Network
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.work.*
import com.example.customnewdemo.R
import com.example.customnewdemo.databinding.ActivityWorkBinding
import com.example.customnewdemo.jtpack.workmanager.WorkA
import com.example.customnewdemo.jtpack.workmanager.WorkB
import com.example.customnewdemo.jtpack.workmanager.WorkDemo
import com.example.customnewdemo.utils.LogUtils
import java.util.concurrent.TimeUnit

/**
 * workmanager
 */
class WorkActivity : AppCompatActivity() {


    companion object {

        val TAG = "WorkActivity"
    }

    private lateinit var mbinding: ActivityWorkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivityWorkBinding.inflate(layoutInflater)
        setContentView(mbinding.root)

        var handler = Handler(mainLooper)
        var msg = Message()
        msg.obj ="哈哈"
        handler.sendMessage(msg)
        initWork()
//        initWork2()
    }

    private fun initWork2() {
        val A = OneTimeWorkRequestBuilder<WorkA>() // 一次性任务.
//            .setConstraints(constraints) // 设置网络条件
            .setInputData(workDataOf("in_key" to "输入数据"))
            .setInitialDelay(5L, TimeUnit.SECONDS)
            .addTag("workA")
            .build()

        val B = OneTimeWorkRequestBuilder<WorkB>() // 一次性任务.
//            .setConstraints(constraints) // 设置网络条件
            .setInputData(workDataOf("in_key" to "输入数据"))
            .setInitialDelay(5L, TimeUnit.SECONDS)
            .addTag("workB")
            .build()

        val instance = WorkManager.getInstance(this)



        instance.
        getWorkInfosLiveData(WorkQuery.fromIds(A.id,B.id)).observe(this){
            for (workInfo in it) {

                if (workInfo.id == A.id) {
                    LogUtils.d(TAG, "workA 任务 A---")
                    if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                        LogUtils.d(TAG,"workA ---> ${workInfo.outputData.getString("outKeyA")}")
                    }
                }

                if (workInfo.id == B.id) {
                    LogUtils.d(TAG, "workA 任务 B---")
                    if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                        LogUtils.d(TAG,"workB ---> ${workInfo.outputData.getString("outKeyB")}")
                    }
                }
            }
        }

        var enqueue = WorkManager.getInstance(this).beginWith(A).then(B)
        var  list = arrayListOf<WorkContinuation>()
        list.add(enqueue)
        WorkContinuation.combine(mutableListOf(enqueue))
            .enqueue()


    }

    private fun initWork() {


        val constraints = Constraints.Builder()
            // NetworkType.METERED 数据流量下执行
            //NetworkType.NOT_REQUIRED 无网络要求
            //NetworkType.UNMETERED 无计费网络 wifi
            //NetworkType.NOT_ROAMING 非漫游网络
            // NetworkType.CONNECTED 网络时候执行
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<WorkDemo>() // 一次性任务.
            .setConstraints(constraints) // 设置网络条件
            .setInputData(workDataOf("in_key" to "输入数据"))
            .setInitialDelay(5L, TimeUnit.SECONDS)
            .addTag("storm")
            .build()

        val instance = WorkManager.getInstance(this)

        instance.getWorkInfoByIdLiveData(workRequest.id)
            .observe(this) {
                LogUtils.d(TAG, "onCreate --" + it.state)
                if (it.state == WorkInfo.State.SUCCEEDED)
                    LogUtils.d(TAG, "${it.outputData.getString("out_key")}")
            }

        instance.enqueue(workRequest)


    }
}