package com.horse.proud.data.model.rental

import com.google.gson.annotations.SerializedName

/**
 * @author liliyuan
 * @since 2020年4月26日15:15:17
 * */
class RentalList {

    @SerializedName("data")
    lateinit var rentalList: ArrayList<RentalItem>

}