package com.horse.proud.data.model.login

import com.google.gson.annotations.SerializedName

/**
 * 调用百度API的token
 *
 * @author liliyuan
 * @since 2020年5月31日16:53:47
 * */
class Token {

    /**
     * 要获取的Access Token
     * */
    @SerializedName("access_token")
    var token:String = ""

    @SerializedName("expires_in")
    var validity:String = ""

    @SerializedName("error")
    var error:String = ""

    @SerializedName("error_description")
    var des:String = ""

}