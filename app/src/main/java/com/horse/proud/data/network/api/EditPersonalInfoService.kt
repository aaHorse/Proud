package com.horse.proud.data.network.api

import com.horse.proud.data.model.Response
import com.horse.proud.data.model.regist.Register
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author liliyuan
 * @since 2020年6月4日19:28:28
 * */
interface EditPersonalInfoService {

    @POST("api/user/update")
    fun update(@Body register: Register): Call<Response>

}