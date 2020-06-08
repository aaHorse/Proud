package com.horse.proud.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit 2
 *
 * @author liliyuan
 * @since 2020年4月26日12:35:01
 * */
object ServiceCreator {

    //private const val BASE_URL = "http://47.95.3.253:8080/space/"

    private const val BASE_URL = "http://47.100.251.3/space/"

    private val httpClient = OkHttpClient.Builder()
//        .readTimeout(5, TimeUnit.MINUTES)
//        .connectTimeout(5, TimeUnit.MINUTES)
//        .writeTimeout(300, TimeUnit.MINUTES)

    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient.build())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())


    private val retrofit = builder.build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

}