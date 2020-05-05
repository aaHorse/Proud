package com.horse.proud.data.model.lost

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.horse.proud.data.model.other.CommentItem
import java.util.ArrayList

/**
 * 失物招领实体类
 *
 * @author liliyuan
 * @since 2020年4月26日15:13:46
 * */
class LostItem : Parcelable {

    @SerializedName("userId")
    var userId:Int = 0

    lateinit var title:String

    lateinit var content:String

    lateinit var image:String

    lateinit var label:String

    lateinit var location:String

    @SerializedName("isLost")
    var isLost:Int = -1

    @SerializedName("isDone")
    var done:Int = -1

    @SerializedName("time")
    lateinit var time:String

    @SerializedName("thumbUp")
    var thumbUp:Int = 0

    @SerializedName("collect")
    var collect:Int = 0

    @SerializedName("comment")
    var comment:Int = 0

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
//        dest.writeString(name)
//        dest.writeString(content)
//        dest.writeStringList(photos)
    }

    constructor() {}

    protected constructor(parcel: Parcel) {
//        content = parcel.readString()
//        photos = parcel.createStringArrayList() as ArrayList<String>
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