package com.horse.proud.data.network.api

import com.horse.proud.data.model.Response
import com.horse.proud.data.model.task.TaskItem
import com.horse.proud.data.model.task.TaskList
import com.horse.proud.data.model.task.TaskPublish
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
interface TaskService {

    @POST("api/task/insert")
    fun publish(@Body task:TaskItem): Call<TaskPublish>

    @GET("api/task/query/all")
    fun getAll():Call<TaskList>

    @Multipart
    @POST("upload/task/setFileUpload")
    fun uploadImage(@Part part: MultipartBody.Part,@Part("id")requestBody: RequestBody): Call<Response>

    @FormUrlEncoded
    @POST("api/task/update/thumb_up")
    fun like(@Field("id")id:String):Call<Response>

}