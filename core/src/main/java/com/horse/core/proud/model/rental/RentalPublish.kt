package com.horse.core.proud.model.rental

import com.google.gson.annotations.SerializedName
import com.horse.core.proud.model.Response

/**
 * @author liliyuan
 * @since 2020年5月5日13:18:37
 * */
class RentalPublish : Response(){

    @SerializedName("data")
    lateinit var data:String

}