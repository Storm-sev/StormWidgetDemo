package com.example.customnewdemo.jtpack.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.customnewdemo.utils.LogUtils


class WorkDemo(context: Context, params: WorkerParameters) : Worker(context, params) {

    companion object {
        val TAG = "WorkDemo"
    }

    override fun doWork(): Result {

        val in_value = inputData.getString("in_key")
        LogUtils.d(TAG, "任务执行开始 + 传入的参数 ---> ${in_value}")
        LogUtils.d(TAG, "任务执行结束")

        return Result.success(workDataOf("out_key" to "执行成功"))

    }
}


class WorkA(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {


        LogUtils.d(WorkDemo.TAG, "work A ->任务执行开始 ")
        LogUtils.d(WorkDemo.TAG, "work A -> 任务执行结束")
        return Result.success(workDataOf("outKeyA" to "workA 完成"))
    }


}

class WorkB(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        LogUtils.d(WorkDemo.TAG, "work B->任务执行开始 ")
        LogUtils.d(WorkDemo.TAG, "work B -> 任务执行结束")
        return Result.success(workDataOf("outKeyB" to "workB 完成"))
    }


}