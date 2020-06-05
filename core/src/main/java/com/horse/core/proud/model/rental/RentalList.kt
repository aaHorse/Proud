package com.horse.core.proud.model.rental

import com.google.gson.annotations.SerializedName
import com.horse.core.proud.model.Response

/**
 * @author liliyuan
 * @since 2020年4月26日15:15:17
 * */
class RentalList: Response() {

    @SerializedName("data")
    var rentalList: ArrayList<RentalItem> = ArrayList()

}