package com.horse.proud.data.network.api

import com.horse.proud.data.model.Response
import com.horse.proud.data.model.rental.RentalItem
import com.horse.proud.data.model.rental.RentalList
import com.horse.proud.data.model.rental.RentalPublish
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
interface RentalService {

    @POST("api/rental/insert")
    fun publish(@Body item: RentalItem): Call<RentalPublish>

    @GET("api/rental/query/all")
    fun getAll():Call<RentalList>

    @Multipart
    @POST("upload/good/setFileUpload")
    fun uploadImage(@Part part: MultipartBody.Part, @Part("id")requestBody: RequestBody): Call<Response>

    @GET("api/rental/update/thumbUp")
    fun like(@Query("id")id:String):Call<Response>

}