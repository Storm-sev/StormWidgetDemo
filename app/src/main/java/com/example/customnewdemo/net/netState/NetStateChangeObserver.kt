package com.example.customnewdemo.net.netState

/**
 *  网络状态观察者
 */
interface NetStateChangeObserver {

    fun onNetDisconnected()

    fun onNetConnected(netWorkType: NetworkUtils.NetworkType)
}