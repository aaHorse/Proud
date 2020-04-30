package com.horse.proud.bean.base

import com.google.gson.annotations.SerializedName

/**
 * 主界面，发布任务、失物招领、物品租赁的 Item 基类
 *
 * @author liliyuan
 * @since 2020年4月25日07:33:37
 * */
abstract class BaseFeed : Model(){

    override val modelId: Long
        get() = id

    //待交互时完善
    @SerializedName("id")
    var id: Long = 0;

}