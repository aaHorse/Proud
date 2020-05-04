package com.horse.proud.data.model.login

import com.google.gson.annotations.SerializedName
import com.horse.proud.data.model.Response

/**
 * 登录
 *
 * @author liliyuan
 * @since 2020年5月2日14:32:07
 * */
class Login:Response(){

    @SerializedName("data")
    var data:Int = -1

}