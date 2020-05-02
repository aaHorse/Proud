package com.horse.proud.data.network.api

import com.horse.proud.data.model.login.Login
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * 登录和注册
 *
 * @author liliyuan
 * @since 2020年5月2日15:09:14
 * */
interface LoginService {

    @FormUrlEncoded
    @POST("user/register")
    fun login(@Field("username")username:String,@Field("password")password:String,@Field("repassword")repassword:String): Call<Login>

}