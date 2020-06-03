package com.horse.proud.data.network.api

import com.horse.proud.data.model.init.CheckNewVersion
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author liliyuan
 * @since 2020年6月2日15:26:52
 * */
interface SplashService {

    @GET("api/edition/checkForNew")
    fun checkNewVersion(@Query("edition")tempVersion:String): Call<CheckNewVersion>

}