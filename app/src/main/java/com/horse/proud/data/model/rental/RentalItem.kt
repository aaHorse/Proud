package com.horse.proud.data.model.rental

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.horse.proud.data.model.other.CommentItem
import com.horse.proud.data.model.other.CommentList

/**
 * 物品租赁
 *
 * @author liliyuan
 * @since 2020年4月26日15:14:20
 * */
class RentalItem : Parcelable {

    lateinit var id:String

    @SerializedName("userId")
    var userId:Int = 0

    lateinit var title:String

    lateinit var content:String

    var image:String ?= null

    lateinit var label:String

    lateinit var location:String

    var unitPrice:Double = 0.00

    var newDegree:Int = 90

    @SerializedName("isDone")
    var done:Int = -1

    @SerializedName("reword")
    lateinit var reword:String

    @SerializedName("startTime")
    lateinit var startTime:String

    @SerializedName("endTime")
    lateinit var endTime:String

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