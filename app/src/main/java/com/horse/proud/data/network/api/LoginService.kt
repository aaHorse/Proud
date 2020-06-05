package com.horse.proud.data.network.api

import com.horse.core.proud.model.Response
import com.horse.core.proud.model.login.Login
import com.horse.core.proud.model.login.Token
import com.horse.core.proud.model.login.WordsResult
import com.horse.core.proud.model.regist.Register
import retrofit2.Call
import retrofit2.http.*

/**
 * 登录和注册，两个功能对应的端口放在了一起
 *
 * @author liliyuan
 * @since 2020年5月2日15:09:14
 * */
interface LoginService {

    @FormUrlEncoded
    @POST("api/user/login")
    fun login(@Field("accountNumber")accountNumber:String,@Field("password")password:String): Call<Login>

    @POST("api/user/register")
    fun register(@Body register: Register): Call<Response>

}