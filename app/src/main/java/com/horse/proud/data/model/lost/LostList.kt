package com.horse.proud.data.model.lost

import com.google.gson.annotations.SerializedName
import com.horse.proud.data.model.Response

/**
 * @author liliyuan
 * @since 2020年4月26日15:14:52
 * */
class LostList:Response() {

    @SerializedName("data")
    lateinit var lostList: ArrayList<LostItem>

}