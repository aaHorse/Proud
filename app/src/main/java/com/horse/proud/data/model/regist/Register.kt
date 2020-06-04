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

    var id = 0

    @SerializedName("accountNumber")
    var number = ""

    @SerializedName("password")
    var password = ""

    @SerializedName("name")
    var name = ""

    @SerializedName("sex")
    var sex:Int = 0

    @SerializedName("phoneNumber")
    var phoneNumber = "10086"

    /**
     * 头像
     * */
    @SerializedName("head")
    var head = ""

    @SerializedName("address")
    var address = ""

    /**
     * 一句话简介
     * */
    @SerializedName("info")
    var info = "太强了"

    var token = ""

}