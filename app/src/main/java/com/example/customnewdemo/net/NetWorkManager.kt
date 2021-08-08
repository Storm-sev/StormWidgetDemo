package com.example.customnewdemo.net

import android.content.Context
import com.example.customnewdemo.common.BaseConstant
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class NetWorkManager private constructor() {


    companion object {

        val CONNECT_TIMEOUT = 10L
        val READ_TIMEOUT = 10L
        val WRITE_TIMEOUT = 10L


        val instance: NetWorkManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetWorkManager()
        }
    }

    private var BASE_URL = ""

    private var context: Context? = null
    public fun init(context: Context): NetWorkManager {
        this.context = context
        return instance
    }

    public fun baseUrl(baseUrl: String) {
        BASE_URL = baseUrl
    }


    val retrofit: Retrofit by lazy {

        if (this.BASE_URL.isEmpty()) {
            throw Exception("you are not set base_url please set")
        }

        val retrofit =
            Retrofit.Builder()
                .baseUrl(BaseConstant.BASE_URL)
                .client(okHttpClient)
//                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        retrofit
    }


    val okHttpClient: OkHttpClient by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .retryOnConnectionFailure(true)
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
        okHttpClient
    }

    /**
     *  log 拦截器
     */
    val httpLoggingInterceptor: HttpLoggingInterceptor by lazy {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        loggingInterceptor
    }


    val cache: Cache by lazy {

        if (null == context) {
            throw Exception("you should apply init() with start")
        }

        Cache(File(context!!.cacheDir.toString(), "http_cache"), 1024 * 1024 * 100)
    }


    /**
     * 创建一个seivice
     *
     */
    public fun <T> createHttpService(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }

}