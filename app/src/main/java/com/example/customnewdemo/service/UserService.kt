package com.example.customnewdemo.service

import com.example.customnewdemo.bean.UserHeadBean
import io.reactivex.rxjava3.core.Observable

import retrofit2.http.GET


interface UserService {

    @GET("api/columns/zhihuadmin")
    fun getUserHead(): Observable<UserHeadBean>

}