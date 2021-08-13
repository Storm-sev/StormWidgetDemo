package com.example.customnewdemo.net.netState

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager

/**
 * 广播通知
 */
class NetStateChangeReceiver : BroadcastReceiver() {

    companion object {


        /**
         * 注册广播通知
         */
        public fun registerReceiver(context: Context?) {

            context?.let {
                val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                it.registerReceiver(InstanceHolder.INSTANCE, intentFilter)
            }
        }

        /**
         * 解注册
         */
        public fun unRegisterRecycler(context: Context?) {
            context?.let {
                it.unregisterReceiver(InstanceHolder.INSTANCE)

            }
        }


        /**
         * 观察这注册
         */
        public fun registerObserver(observer: NetStateChangeObserver?) {
            observer?.let {
                if (!InstanceHolder.INSTANCE.mObservers.contains(it)) {
                    InstanceHolder.INSTANCE.mObservers.add(it)
                }
            }
        }


        public fun unRegisterObserver(observer: NetStateChangeObserver?) {

            observer?.let {
                if (InstanceHolder.INSTANCE.mObservers == null) {
                    return
                }

                InstanceHolder.INSTANCE.mObservers.remove(it)

            }
        }

    }


    private val mObservers: ArrayList<NetStateChangeObserver> = arrayListOf()

    override fun onReceive(context: Context?, intent: Intent?) {


        intent?.let {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(it.action)) {
                var networkType = NetworkUtils.networkType
                notifyObservers(networkType)
            }
        }

    }


    private var mType = NetworkUtils.networkType
    private fun notifyObservers(networkType: NetworkUtils.NetworkType) {

        if (mType == networkType) {
            return
        }
        mType = networkType

        if (networkType == NetworkUtils.NetworkType.NETWORK_NO) {
            //无网络
            for (observer in mObservers) {
                observer.onNetDisconnected()
            }
        } else {
            for (observer in mObservers) {
                observer.onNetConnected(mType)

            }
        }

    }


    class InstanceHolder {
        companion object {
            public val INSTANCE: NetStateChangeReceiver = NetStateChangeReceiver()

        }
    }


}