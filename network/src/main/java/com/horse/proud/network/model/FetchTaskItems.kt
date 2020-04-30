package com.horse.proud.network.model

import com.google.gson.annotations.SerializedName
import com.horse.proud.bean.Task
import com.horse.proud.network.model.base.Callback
import com.horse.proud.network.model.base.Response
import com.horse.proud.network.request.FetchTaskItemsRequest

/**
 * 获取任务列表
 *
 * @author liliyuan
 * @since 2020年4月25日18:11:38
 * */
class FetchTaskItems : Response() {

    @SerializedName("data")
    var items: MutableList<Task> = ArrayList()

    companion object{

        fun getResponse(callback: Callback) {
            FetchTaskItemsRequest()
                .listen(callback)
        }

        fun getResponse(lastFeed: Long, callback: Callback) {
            FetchTaskItemsRequest()
                .lastFeed(lastFeed)
                .listen(callback)
        }

    }

}