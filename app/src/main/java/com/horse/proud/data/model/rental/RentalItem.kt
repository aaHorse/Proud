package com.horse.proud.data.model.rental

import android.os.Parcel
import android.os.Parcelable

/**
 * 物品租赁
 *
 * @author liliyuan
 * @since 2020年4月26日15:14:20
 * */
class RentalItem : Parcelable {

    var name:String? = null

    var content: String? = "nulffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffl"

    var photos: ArrayList<String> = arrayListOf<String>("http://bgashare.bingoogolapple.cn/refreshlayout/images/staggered1.png","http://bgashare.bingoogolapple.cn/refreshlayout/images/staggered1.png")



    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(content)
        dest.writeStringList(photos)
    }

    constructor() {}

    protected constructor(parcel: Parcel) {
        content = parcel.readString()
        photos = parcel.createStringArrayList() as ArrayList<String>
    }

    companion object {
        val CREATOR: Parcelable.Creator<RentalItem?> = object : Parcelable.Creator<RentalItem?> {
            override fun createFromParcel(source: Parcel): RentalItem? {
                return RentalItem(source)
            }

            override fun newArray(size: Int): Array<RentalItem?> {
                return arrayOfNulls(size)
            }
        }
    }

}