package com.horse.core.proud.model.rental

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.horse.core.proud.model.other.CommentItem
import com.horse.core.proud.model.other.CommentList

/**
 * 物品租赁
 *
 * @author liliyuan
 * @since 2020年4月26日15:14:20
 * */
class RentalItem : Parcelable {

    var id = ""

    @SerializedName("userId")
    var userId:Int = 0

    var title = ""

    var content = ""

    var image = ""

    var label = ""

    var location = ""

    var unitPrice:Double = 0.00

    var newDegree:Int = 90

    @SerializedName("isDone")
    var done:Int = -1

    @SerializedName("reword")
    var reword = ""

    @SerializedName("startTime")
    var startTime = ""

    @SerializedName("endTime")
    var endTime = ""

    @SerializedName("thumbUp")
    var thumbUp:Int = 0

    @SerializedName("collect")
    var collect:Int = 0

    @SerializedName("comment")
    var comment:Int = 0

    /**
     * 评论内容，在另外一个接口获取到，然后在这里赋值
     * */
    var comments: CommentList?=null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeInt(userId)
        dest.writeString(title)
        dest.writeString(content)
        dest.writeString(image)
        dest.writeString(label)
        dest.writeString(location)
        dest.writeDouble(unitPrice)
        dest.writeInt(newDegree)
        dest.writeInt(done)
        dest.writeString(startTime)
        dest.writeString(endTime)
    }

    constructor() {}

    constructor(parcel: Parcel) {
        id = parcel.readString() ?: ""
        userId = parcel.readInt()
        title = parcel.readString() ?: ""
        content = parcel.readString() ?: ""
        image = parcel.readString() ?: ""
        label = parcel.readString() ?: ""
        location = parcel.readString() ?: ""
        unitPrice = parcel.readDouble()
        newDegree = parcel.readInt()
        done = parcel.readInt()
        startTime = parcel.readString() ?: ""
        endTime = parcel.readString() ?: ""
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<RentalItem?> = object : Parcelable.Creator<RentalItem?> {
            override fun createFromParcel(source: Parcel): RentalItem? {
                return RentalItem(source)
            }

            override fun newArray(size: Int): Array<RentalItem?> {
                return arrayOfNulls(size)
            }
        }
    }

}