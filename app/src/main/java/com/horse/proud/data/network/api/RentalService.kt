package com.horse.proud.data.network.api

import com.horse.proud.data.model.rental.RentalList
import com.horse.proud.data.model.task.TaskList
import retrofit2.Call
import retrofit2.http.GET

/**
 * 任务接口
 *
 * @author liliyuan
 * @since 2020年4月26日12:36:41
 * */
interface RentalService {

    @GET("wxarticle/chapters/json ")
    fun getTask(): Call<RentalList>

}