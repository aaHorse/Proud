package com.horse.core.proud.model.lost

import com.google.gson.annotations.SerializedName
import com.horse.core.proud.model.Response

/**
 * @author liliyuan
 * @since 2020年5月5日13:09:58
 * */
class LostPublish : Response(){

    @SerializedName("data")
    lateinit var data:String

}