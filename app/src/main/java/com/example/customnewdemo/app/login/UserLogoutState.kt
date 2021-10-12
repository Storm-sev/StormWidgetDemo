package com.example.customnewdemo.app.login

import com.example.customnewdemo.utils.LogUtils

/**
 * 用户 未登录 状态
 *
 */
class UserLogoutState : UserState {

    companion object{
        val TAG ="UserLogoutState"
    }


    override fun pay() {

        LogUtils.d(TAG,"未登录不能进行支付")

        getLoginActivity();
    }



    override fun collection() {
        LogUtils.d(TAG,"未登录不能进行收藏 ... ")
    }

    private fun getLoginActivity() {

        LogUtils.d(TAG,"跳转到的登录页面")
    }
}