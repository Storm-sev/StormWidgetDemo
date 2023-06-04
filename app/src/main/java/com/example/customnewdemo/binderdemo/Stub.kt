package com.example.customnewdemo.binderdemo

import android.os.Binder
import android.os.IBinder
import android.os.Parcel
import android.view.SurfaceControl.Transaction
import android.widget.Switch
import com.example.customnewdemo.act.REQUEST_CODE
import java.lang.reflect.Proxy

open abstract class Stub : Binder, IBinderFunManager {
    companion object {

        val DESCRIPTOR = "com.storm.customnewdemo.BinderFunManager"
        val TRANSACTION_FUN = IBinder.FIRST_CALL_TRANSACTION;
        val GET_REQUEST = IBinder.FIRST_CALL_TRANSACTION + 1;

        //
        public fun asInterface(binder: IBinder?): com.example.customnewdemo.binderdemo.Proxy? {
            if (binder == null) return null
            var iin = binder.queryLocalInterface(DESCRIPTOR)
            iin?.let {
                if (it is IBinderFunManager) {
                    return com.example.customnewdemo.binderdemo.Proxy(binder)

                }
            }
            return null
        }
    }


    override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {

        when (code) {
            INTERFACE_TRANSACTION -> {
                reply?.writeString(DESCRIPTOR)
                return true
            }

            TRANSACTION_FUN -> {
                data.enforceInterface(DESCRIPTOR)
                val str = data.readString()
                this.FUN(str)
                reply?.writeNoException()
                return true
            }

            GET_REQUEST -> {
                data.enforceInterface(DESCRIPTOR)
                val code = data.readInt()
                this.getRequest(code)
                reply?.writeNoException()
                return true
            }

        }

        return super.onTransact(code, data, reply, flags)
    }


    constructor() {
        this.attachInterface(this, DESCRIPTOR)
    }


    override fun asBinder(): IBinder {
        return this
    }


}