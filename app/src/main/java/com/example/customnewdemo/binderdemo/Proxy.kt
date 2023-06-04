package com.example.customnewdemo.binderdemo

import android.os.IBinder
import android.os.Parcel

class Proxy(private var remote: IBinder) : IBinderFunManager {

    companion object {
        val DESCRIPTOR = "com.storm.customnewdemo.BinderFunManager"


    }

    public fun getInterfaceDescriptor():String{
        return DESCRIPTOR;
    }


    override fun FUN(str: String?) {
        val data = Parcel.obtain()
        val replay = Parcel.obtain()

        try {
            data.writeInterfaceToken(DESCRIPTOR)
            data.writeString(str)
            remote.transact(Stub.TRANSACTION_FUN, data, replay, 0)
            replay.readException()

        }finally {
            data.recycle()
            replay.recycle()
        }
    }

    override fun getRequest(code: Int) {
        val data = Parcel.obtain()
        val replay = Parcel.obtain()

        try {
            data.writeInterfaceToken(DESCRIPTOR)
            data.writeInt(code)
            remote.transact(Stub.GET_REQUEST, data, replay, 0)
            replay.readException()

        }finally {
            data.recycle()
            replay.recycle()
        }
    }

    override fun asBinder(): IBinder {
        return remote
    }


}