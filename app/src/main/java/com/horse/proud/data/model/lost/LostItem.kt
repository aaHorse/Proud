package com.horse.proud.data.model.lost

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

/**
 * 失物招领实体类
 *
 * @author liliyuan
 * @since 2020年4月26日15:13:46
 * */
class LostItem : Parcelable {

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
        val CREATOR: Parcelable.Creator<LostItem?> = object : Parcelable.Creator<LostItem?> {
            override fun createFromParcel(source: Parcel): LostItem? {
                return LostItem(source)
            }

            override fun newArray(size: Int): Array<LostItem?> {
                return arrayOfNulls(size)
            }
        }
    }

}