package com.horse.proud.data.network.api

import com.horse.core.proud.model.Response
import com.horse.core.proud.model.other.CommentItem
import com.horse.core.proud.model.other.CommentList
import com.horse.core.proud.model.task.TaskItem
import com.horse.core.proud.model.task.TaskList
import com.horse.core.proud.model.task.TaskPublish
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
    fun publish(@Body task: TaskItem): Call<TaskPublish>

    @GET("api/task/query/all")
    fun getAll():Call<TaskList>

    @Multipart
    @POST("upload/task/setFileUpload")
    fun uploadImage(@Part part: MultipartBody.Part,@Part("id")requestBody: RequestBody): Call<Response>

    @FormUrlEncoded
    @POST("api/task/update/thumb_up")
    fun like(@Field("id")id:String):Call<Response>

    @GET("api/query/task/comment/table_id/{path}")
    fun getComments(@Path("path")table_id:String):Call<CommentList>

    @POST("api/task/comment/insert")
    fun publishComment(@Body comment: CommentItem):Call<Response>

    @GET("api/task/query/user_id/{path}")
    fun userTask(@Path("path")id:Int):Call<TaskList>

}