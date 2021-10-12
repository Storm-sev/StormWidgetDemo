package com.example.customnewdemo.app.login

import com.example.customnewdemo.utils.LogUtils

/**
 * 用户已经登录状态
 */
class UserLoginInState : UserState {

    companion object{
        val TAG = "UserLoginInState"
    }


    override fun pay() {
        LogUtils.d(TAG,"进行支付 ....")

    }

    override fun collection() {
        LogUtils.d(TAG,"执行收藏操作  ....")
    }
}