package com.horse.proud.data.network.api

import com.horse.core.proud.model.Response
import com.horse.core.proud.model.other.CommentItem
import com.horse.core.proud.model.other.CommentList
import com.horse.core.proud.model.rental.RentalItem
import com.horse.core.proud.model.rental.RentalList
import com.horse.core.proud.model.rental.RentalPublish
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.ArrayList

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

    @Multipart
    @POST("upload/good/setFilesUpload")
    fun uploadImages(@Part parts: ArrayList<MultipartBody.Part>, @Part("id")requestBody: RequestBody): Call<Response>

    @GET("api/rental/update/thumbUp")
    fun like(@Query("id")id:String):Call<Response>

    @GET("api/query/good/comment/table_id/{path}")
    fun getComments(@Path("path")table_id:String):Call<CommentList>

    @POST("api/good/comment/insert")
    fun publishComment(@Body comment: CommentItem):Call<Response>

    @GET("api/rental/query/userId")
    fun userRental(@Query("userId")id:Int):Call<RentalList>

    @POST("api/rental/update")
    fun update(@Body task: RentalItem):Call<Response>

    @POST("api/rental/delete")
    fun delete(@Body item: RentalItem):Call<Response>

}