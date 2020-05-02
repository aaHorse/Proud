package com.horse.proud.data.model.regist

import com.google.gson.annotations.SerializedName
import com.horse.proud.data.model.Base

/**
 * 注册
 *
 * @author liliyuan
 * @since 实践
 * */
class Register: Base() {

    @SerializedName("accountNumber")
    lateinit var number:String

    @SerializedName("password")
    lateinit var password:String

    @SerializedName("name")
    lateinit var name:String

    @SerializedName("sex")
    var sex:Int = 0

    @SerializedName("phoneNumber")
    lateinit var phoneNumber:String

    /**
     * 头像
     * */
    @SerializedName("head")
    lateinit var head:String

    @SerializedName("address")
    lateinit var address:String

    /**
     * 一句话简介
     * */
    @SerializedName("info")
    lateinit var info:String

}