package com.horse.core.proud.model.task

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.horse.core.proud.model.other.CommentItem
import com.horse.core.proud.model.other.CommentList
import java.util.*
import kotlin.collections.ArrayList

/**
 * 任务列表
 *
 * @author liliyuan
 * @since 2020年4月30日11:45:37
 * */
class TaskItem : Parcelable {

    var id = ""

    @SerializedName("user_id")
    var userId = 0

    var title = ""

    var content = ""

    var image = ""

    var images: ArrayList<String>
        get(){
            val temp = ArrayList<String>()
            if(image.contains(Regex(".*,"))){
                //多图
                temp.addAll(image.split(",") - "")
            }else{
                //单图
                temp.add(image)
            }
            return temp
        }
        set(value) {
            //
        }

    var label = ""

    var location = ""

    @SerializedName("is_done")
    var done:Int = -1

    @SerializedName("reword")
    var reword = ""

    @SerializedName("start_time")
    var startTime = ""

    @SerializedName("end_time")
    var endTime = ""

    @SerializedName("thumb_up")
    var thumbUp = 0

    @SerializedName("collect")
    var collect = 0

    @SerializedName("comment")
    var comment = 0

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