package com.example.customnewdemo.act

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customnewdemo.databinding.ActivityCoroutinesBinding
import com.example.customnewdemo.utils.LogUtils
import kotlinx.coroutines.*


/**
 * 协程相关
 */
class CoroutinesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoroutinesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoroutinesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView();
        setUpData();
        setUpListener();
    }

    private fun setUpListener() {


    }

    private fun setUpData() {
//        val context =  CoroutineContext()
//
//        coroutineScope.launch {
//            LogUtils.d(TAG, "运行的线程  ---")
//
//        }


        val coroutineScope = MainScope()
        coroutineScope.launch{
           val str =  getName()
            LogUtils.d(TAG, "获取到的信息 --> $str")


        }

//        CoroutineScope.launch() { }
    }

    private suspend fun getName() = withContext(Dispatchers.IO){
        LogUtils.d(TAG,"在子线程中执行 ")


    }

    private fun setUpView() {


    }

    companion object {

        val TAG = "CoroutinesActivity"

        public fun startSelf(activity: Activity) {
            val intent = Intent(activity, CoroutinesActivity::class.java)
            activity.startActivity(intent)
        }
    }

}