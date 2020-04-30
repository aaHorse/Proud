package com.horse.proud.data.model.lost

import com.google.gson.annotations.SerializedName

/**
 * @author liliyuan
 * @since 2020年4月26日15:14:52
 * */
class LostList {

    @SerializedName("data")
    lateinit var lostList: ArrayList<LostItem>

}