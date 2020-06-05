package com.horse.core.proud.model.lost

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.horse.core.proud.model.other.CommentItem
import com.horse.core.proud.model.other.CommentList
import java.util.ArrayList

/**
 * 失物招领实体类
 *
 * @author liliyuan
 * @since 2020年4月26日15:13:46
 * */
class LostItem : Parcelable {

    var id = ""

    @SerializedName("userId")
    var userId:Int = 0

    var title = ""

    var content = ""

    var image = ""

    var label = ""

    var location = ""

    /**
     * lost:0
     * found:-1
     * */
    @SerializedName("isLost")
    var isLost:Int = -2

    @SerializedName("isDone")
    var done:Int = -1

    @SerializedName("time")
    var time = ""

    @SerializedName("thumbUp")
    var thumbUp:Int = 0

    @SerializedName("collect")
    var collect:Int = 0

    @SerializedName("comment")
    var comment:Int = 0

    override fun describeContents(): Int {
        return 0
    }

    /**
     * 评论内容，在另外一个接口获取到，然后在这里赋值
     * */
    var comments: CommentList?=null

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeInt(userId)
        dest.writeString(title)
        dest.writeString(content)
        dest.writeString(image)
        dest.writeString(label)
        dest.writeString(location)
        dest.writeInt(isLost)
        dest.writeInt(done)
        dest.writeString(time)
    }

    constructor() {}

    constructor(parcel: Parcel) {
        id= parcel.readString() ?: ""
        userId = parcel.readInt()
        title = parcel.readString() ?: ""
        content = parcel.readString() ?: ""
        image = parcel.readString() ?: ""
        label = parcel.readString() ?: ""
        location = parcel.readString() ?: ""
        isLost = parcel.readInt()
        done = parcel.readInt()
        time = parcel.readString() ?: ""

    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<LostItem?> = object : Parcelable.Creator<LostItem?> {
            override fun createFromParcel(source: Parcel): LostItem? {
                return LostItem(source)
            }

            override fun newArray(size: Int): Array<LostItem?> {
                return arrayOfNulls(size)
            }
        }
    }

}