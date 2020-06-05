package com.horse.core.proud.model.task

import com.google.gson.annotations.SerializedName
import com.horse.core.proud.model.Response

/**
 * @author liliyuan
 * @since 2020年4月26日12:43:55
 * */
class TaskList: Response() {

    @SerializedName("data")
    var taskList: ArrayList<TaskItem> = ArrayList()

}