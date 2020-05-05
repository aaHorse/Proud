package com.horse.proud.data.model.rental

import com.google.gson.annotations.SerializedName
import com.horse.proud.data.model.Response

/**
 * @author liliyuan
 * @since 2020年5月5日13:18:37
 * */
class RentalPublish : Response(){

    @SerializedName("data")
    lateinit var data:String

}