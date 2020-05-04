package com.horse.proud.data.network.api

import com.horse.proud.data.model.task.TaskItem
import com.horse.proud.data.model.task.TaskList
import com.horse.proud.data.model.task.TaskPublish
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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

}