package com.horse.core.proud.model

import com.google.gson.annotations.SerializedName

/**
 * 数据实体基类
 *
 * @author liliyuan
 * @since 2020年5月2日14:26:10
 * */
open class Response {

    @SerializedName("status")
    var status:Int = 600

    @SerializedName("msg")
    var msg:String = ""

    /**
     * 上传图片用，成功返回 “1000”
     * */
    @SerializedName("code")
    var code:String = "0"

    //lateinit var ok

}