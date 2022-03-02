package com.example.customnewdemo.app.base


data class BaseResp<T>(
    var code: Int = 200,

    var msg: String = "",

    var `data`: T
)

fun <T> BaseResp<T>.dataCover(): T {
    if (code == 0) {
        return data;
    } else {
        throw  Exception(msg)

    }
}
