package com.horse.core.proud.model.lost

import com.google.gson.annotations.SerializedName
import com.horse.core.proud.model.Response

/**
 * @author liliyuan
 * @since 2020年4月26日15:14:52
 * */
class LostList: Response() {

    @SerializedName("data")
    var lostList: ArrayList<LostItem> = ArrayList()

}