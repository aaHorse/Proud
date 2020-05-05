package com.horse.proud.data.model.task

import com.google.gson.annotations.SerializedName
import com.horse.proud.data.model.Response

/**
 * @author liliyuan
 * @since 2020年4月26日12:43:55
 * */
class TaskList:Response() {

    @SerializedName("data")
    lateinit var taskList: ArrayList<TaskItem>

}