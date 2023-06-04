package com.example.customnewdemo.coroutine

import android.content.Context
import android.location.Location
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.example.customnewdemo.utils.LogUtils


import org.jzvd.jzvideo.TAG
import kotlin.math.log


// 协程相关测试

//class CoroutineDemo {
//
//    val add: suspend (CoroutineScope) -> Unit
//       get() = {
//           storm(it)
//       }
//
//    suspend fun storm(coroutineScope: CoroutineScope): Unit{
//        delay(1000)
//    }
//}
//internal class LocationListener(private val context: Context,
//private var lifecycle: Lifecycle,var callBack: (Location) -> Unit): LifecycleObserver{
//
//    @OnLifecycleEvent(value = Lifecycle.Event.ON_START)
//    fun onStart() {
//
//
//    }
//}

internal class LocationService(
    var context: Context,
    var lifecycle: Lifecycle,
    var callBack: (Location) -> Unit
) : DefaultLifecycleObserver {

    init {
        lifecycle.addObserver(this)

    }

    override fun onResume(owner: LifecycleOwner) {

        LogUtils.d(TAG, "执行 onresume 方法的生命周期绑定. ")

        if (lifecycle == owner.lifecycle) {
            LogUtils.d(TAG, "判断当前绑定的生命周期是同一个")

            lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        LogUtils.d(TAG, "执行 onStop 方法的生命周期绑定. ")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        lifecycle.removeObserver(this)

    }
}
