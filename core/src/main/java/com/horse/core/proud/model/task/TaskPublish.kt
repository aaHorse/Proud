package com.horse.core.proud.model.task

import com.google.gson.annotations.SerializedName
import com.horse.core.proud.model.Response

/**
 * @author liliyuan
 * @since 2020年5月4日13:27:56
 * */
class TaskPublish : Response(){

    @SerializedName("data")
    lateinit var data:String

}