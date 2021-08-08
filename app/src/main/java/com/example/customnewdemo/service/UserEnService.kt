package com.example.customnewdemo.service

import com.example.customnewdemo.bean.UserHeadBean
import retrofit2.http.GET

interface UserEnService {
    @GET("api/columns/zhihuadmin")
    suspend fun user(): UserHeadBean
}