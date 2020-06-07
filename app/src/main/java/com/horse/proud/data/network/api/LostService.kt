package com.horse.proud.data.network.api

import com.horse.core.proud.model.Response
import com.horse.core.proud.model.lost.LostItem
import com.horse.core.proud.model.lost.LostList
import com.horse.core.proud.model.lost.LostPublish
import com.horse.core.proud.model.other.CommentItem
import com.horse.core.proud.model.other.CommentList
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.ArrayList

/**
 * 失物招领接口
 *
 * @author liliyuan
 * @since 2020年4月26日12:36:41
 * */
interface LostService {

    @POST("api/lost_and_found/insert")
    fun publish(@Body item: LostItem): Call<LostPublish>

    @GET("api/lost_and_found/query/all")
    fun getAll():Call<LostList>

    /**
     * 单张图片上传
     *
     * @param id 任务的id
     * @param part 图片
     * */
    @Multipart
    @POST("upload/lostfound/setFileUpload")
    fun uploadImage(@Part part: MultipartBody.Part, @Part("id")requestBody: RequestBody): Call<Response>

    /**
     * 多张图片上传
     * */
    @Multipart
    @POST("upload/lostfound/setFilesUpload")
    fun uploadImages(@Part parts: ArrayList<MultipartBody.Part>, @Part("id")requestBody: RequestBody): Call<Response>

    @GET("api/lost_and_found/update/thumbUp")
    fun like(@Query("id")id:String):Call<Response>

    @GET("api/query/lostfound/comment/table_id/{path}")
    fun getComments(@Path("path")table_id:String):Call<CommentList>

    @POST("api/lostfound/comment/insert")
    fun publishComment(@Body comment: CommentItem):Call<Response>

    @GET("api/lost_and_found/query/userId")
    fun userLostAndFound(@Query("userId")id:Int):Call<LostList>

    @POST("api/lost_and_found/update")
    fun update(@Body item: LostItem):Call<Response>

    @POST("api/lost_and_found/delete")
    fun delete(@Body item: LostItem):Call<Response>

}