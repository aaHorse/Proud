package com.horse.proud.data.model.task

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.horse.proud.data.model.other.CommentItem
import com.horse.proud.data.model.other.CommentList
import java.util.*
import kotlin.collections.ArrayList

/**
 * 任务列表
 *
 * @author liliyuan
 * @since 2020年4月30日11:45:37
 * */
class TaskItem : Parcelable {

    lateinit var id:String

    @SerializedName("user_id")
    var userId:Int = 0

    lateinit var title:String

    lateinit var content:String

    var image:String ?= null

    lateinit var label:String

    lateinit var location:String

    @SerializedName("is_done")
    var done:Int = -1

    @SerializedName("reword")
    lateinit var reword:String

    @SerializedName("start_time")
    lateinit var startTime:String

    @SerializedName("end_time")
    lateinit var endTime:String

    @SerializedName("thumb_up")
    var thumbUp:Int = 0

    @SerializedName("collect")
    var collect:Int = 0

    @SerializedName("comment")
    var comment:Int = 0

    /**
     * 评论内容，在另外一个接口获取到，然后在这里赋值
     * */
    var comments:CommentList?=null

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
        done = parcel.readInt()
        startTime = parcel.readString() ?: ""
        endTime = parcel.readString() ?: ""
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<TaskItem?> = object : Parcelable.Creator<TaskItem?> {
            override fun createFromParcel(source: Parcel): TaskItem? {
                return TaskItem(source)
            }

            override fun newArray(size: Int): Array<TaskItem?> {
                return arrayOfNulls(size)
            }
        }
    }

}