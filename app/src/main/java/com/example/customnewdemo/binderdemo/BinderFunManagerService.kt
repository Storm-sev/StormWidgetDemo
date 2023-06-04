package com.example.customnewdemo.binderdemo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.customnewdemo.utils.LogUtils
import org.jzvd.jzvideo.TAG

class BinderFunManagerService   : Stub() {
    override fun FUN(str: String?) {
       LogUtils.d(
           TAG,"string"
       )
    }

    override fun getRequest(code: Int) {
       LogUtils.d("int ")
    }


}