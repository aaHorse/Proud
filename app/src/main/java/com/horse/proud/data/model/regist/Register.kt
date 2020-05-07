package com.horse.proud.data.model.regist

import com.google.gson.annotations.SerializedName
import com.horse.proud.data.model.Response

/**
 * 注册
 *
 * @author liliyuan
 * @since 2020年5月4日08:56:17
 * */
class Register{

    @SerializedName("accountNumber")
    lateinit var number:String

    @SerializedName("password")
    lateinit var password:String

    @SerializedName("name")
    lateinit var name:String

    @SerializedName("sex")
    var sex:Int = 0

    @SerializedName("phoneNumber")
    var phoneNumber:String = "10086"

    /**
     * 头像
     * */
    @SerializedName("head")
    var head:String = ""

    @SerializedName("address")
    var address:String = ""

    /**
     * 一句话简介
     * */
    @SerializedName("info")
    var info:String = "太强了"

}