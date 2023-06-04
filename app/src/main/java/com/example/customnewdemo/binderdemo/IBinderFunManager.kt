package com.example.customnewdemo.binderdemo

import android.os.IInterface
import android.os.RemoteException

interface IBinderFunManager : IInterface {


    @Throws(RemoteException::class )
    fun FUN(str:String?)

    @Throws(RemoteException::class)
    fun getRequest(code: Int)
}