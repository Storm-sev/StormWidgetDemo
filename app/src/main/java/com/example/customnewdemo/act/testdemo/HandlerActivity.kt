package com.example.customnewdemo.act.testdemo

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.customnewdemo.coroutine.LocationService
import com.example.customnewdemo.databinding.ActivityHandlerBinding
import com.example.customnewdemo.utils.LogUtils
import com.example.customnewdemo.utils.text
import kotlinx.coroutines.*

class HandlerActivity : AppCompatActivity() {

    companion object {
        const val TAG = "HandlerActivity"
    }

    lateinit var mBinding: ActivityHandlerBinding
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityHandlerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val locationService = LocationService(this, lifecycle){

        }

        this.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks{
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                LogUtils.d(TAG,"registerActivityLifecycleCallbacks --> onActivityCreated")
            }

            override fun onActivityStarted(activity: Activity) {
                LogUtils.d(TAG,"registerActivityLifecycleCallbacks --> onActivityStarted")
            }

            override fun onActivityResumed(activity: Activity) {
                LogUtils.d(TAG,"registerActivityLifecycleCallbacks --> onActivityResumed")
            }

            override fun onActivityPaused(activity: Activity) {
                LogUtils.d(TAG,"registerActivityLifecycleCallbacks --> onActivityPaused")
            }

            override fun onActivityStopped(activity: Activity) {
                LogUtils.d(TAG,"registerActivityLifecycleCallbacks --> onActivityStopped")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                LogUtils.d(TAG,"registerActivityLifecycleCallbacks --> onActivitySaveInstanceState")
            }

            override fun onActivityDestroyed(activity: Activity) {
                LogUtils.d(TAG,"registerActivityLifecycleCallbacks --> onActivityDestroyed")
            }

        })
        setUp()
    }

    val handler: Handler = Handler(Looper.getMainLooper())
    private fun setUp() {

    }

    override fun onResume() {
        super.onResume()

        setUpCoroutine()



        handler.post {
            LogUtils.d(TAG, "${Thread.currentThread()} ")

            "更改过后的值".text(mBinding.tvContent)

        }
    }

    var scope : CoroutineScope? = null
    private fun setUpCoroutine() {

        scope = MainScope() + CoroutineName(this::class.java.name)
        scope?.launch {

        }
    }

    override fun onStart() {
        super.onStart()



    }


    override fun onDestroy() {
        super.onDestroy()
        scope?.cancel()
    }
}