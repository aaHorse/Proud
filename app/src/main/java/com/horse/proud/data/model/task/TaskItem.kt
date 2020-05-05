package com.horse.proud.data.model.task

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.horse.proud.data.model.other.CommentItem
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

    lateinit var image:String

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
        val CREATOR: Parcelable.Creator<TaskItem?> = object : Parcelable.Creator<TaskItem?> {
            override fun createFromParcel(source: Parcel): TaskItem? {
                return TaskItem(source)
            }

            override fun newArray(size: Int): Array<TaskItem?> {
                return arrayOfNulls(size)
            }
        }
    }

}