package com.horse.proud.data.network.api

import com.horse.core.proud.model.Response
import com.horse.core.proud.model.lost.LostList
import com.horse.core.proud.model.regist.Register
import com.horse.core.proud.model.rental.RentalList
import com.horse.core.proud.model.task.TaskList
import retrofit2.Call
import retrofit2.http.*

/**
 * @author liliyuan
 * @since 2020年6月4日19:28:28
 * */
interface SettingService {

    @POST("api/user/update")
    fun update(@Body register: Register): Call<Response>

    @GET("api/lost_and_found/query/userId")
    fun userLostAndFound(@Query("userId")id:Int):Call<LostList>

    @GET("api/rental/query/userId")
    fun userRental(@Query("userId")id:Int):Call<RentalList>

    @GET("api/task/query/user_id/{path}")
    fun userTask(@Path("path")id:Int):Call<TaskList>

}