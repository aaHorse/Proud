package com.horse.proud.data.model

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
    lateinit var msg:String

    @SerializedName("code")
    lateinit var code:String

    //lateinit var ok

}