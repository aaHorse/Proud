package com.horse.proud.data.network.api

import com.horse.proud.data.model.Response
import com.horse.proud.data.model.lost.LostItem
import com.horse.proud.data.model.lost.LostList
import com.horse.proud.data.model.lost.LostPublish
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * 任务接口
 *
 * @author liliyuan
 * @since 2020年4月26日12:36:41
 * */
interface LostService {

    @POST("api/lost_and_found/insert")
    fun publish(@Body item:LostItem): Call<LostPublish>

    @GET("api/lost_and_found/query/all")
    fun getAll():Call<LostList>

    @Multipart
    @POST("upload/lostfound/setFileUpload")
    fun uploadImage(@Part part: MultipartBody.Part, @Part("id")requestBody: RequestBody): Call<Response>

    @GET("api/lost_and_found/update/thumbUp")
    fun like(@Query("id")id:String):Call<Response>

}