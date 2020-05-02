package com.horse.proud.data.model

import com.google.gson.annotations.SerializedName

/**
 * 数据实体基类
 *
 * @author liliyuan
 * @since 2020年5月2日14:26:10
 * */
open class Base {

    @SerializedName("status")
    var status:Long = 500

    @SerializedName("msg")
    lateinit var msg:String

    //lateinit var ok

}