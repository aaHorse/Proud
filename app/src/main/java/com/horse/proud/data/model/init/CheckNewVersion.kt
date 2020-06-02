package com.horse.proud.data.model.init

import com.google.gson.annotations.SerializedName
import com.horse.proud.data.model.Response

/**
 * 新版本检测
 *
 * @author liliyuan
 * @since 2020年6月2日15:23:47
 * */
class CheckNewVersion :Response(){

    @SerializedName("data")
    var data:String = ""

}