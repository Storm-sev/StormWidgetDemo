package com.example.customnewdemo.act.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customnewdemo.bean.UserHeadBean
import com.example.customnewdemo.net.NetWorkManager
import com.example.customnewdemo.service.UserEnService
import com.example.customnewdemo.service.UserService

import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    companion object {
        val TAG = "MainViewModel -->"
    }

//    val userhead: MutableLiveData<UserHeadBean> by lazy {
//        MutableLiveData<UserHeadBean>().also {
//            loadUserHead()
//        }
//    }

    val userhead: MutableLiveData<UserHeadBean> = MutableLiveData()
    val curPage : Int = 1;
    /**
     * 加载头像
     */
    public fun loadUserHead() {
        Log.d(TAG, "执行网络请求")

        val userService = NetWorkManager.instance.createHttpService(UserService::class.java)

        requestUserHead(userService);

//        val userService = NetWorkManager.instance.createHttpService(UserService::class.java)
//
//        userService.getUserHead()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object:io.reactivex.rxjava3.core.Observer<UserHeadBean>{
//                override fun onError(e: Throwable?) {
//                }
//
//                override fun onSubscribe(d: Disposable?) {
//
//                }
//
//                override fun onNext(t: UserHeadBean?) {
//                    t?.let {
//                        userhead.value = it
//                    }
//                }
//
//                override fun onComplete() {
//
//                }
//
//            })


    }

    private fun requestUserHead(userService: UserService) {

        viewModelScope.launch {
            try {

                val service = NetWorkManager.instance.createHttpService(UserEnService::class.java)
                var data:UserHeadBean = service.user()

                // 可以正常的code 判断

                userhead.value = data

            } catch (e: Exception) {

            }
        }

    }


    fun updateValue() {

        val value = userhead.value as UserHeadBean
        (userhead.value as UserHeadBean).description = "strom fuck"
        userhead.value = userhead.value

//        userhead.value = value
    }
}