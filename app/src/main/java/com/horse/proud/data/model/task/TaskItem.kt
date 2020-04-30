package com.horse.proud.data.model.task

import android.os.Parcel
import android.os.Parcelable
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

    var name:String? = null

    var content: String? = "nulfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffl"

    var photos: ArrayList<String> = arrayListOf("http://bgashare.bingoogolapple.cn/refreshlayout/images/staggered1.png"
        ,"http://bgashare.bingoogolapple.cn/refreshlayout/images/staggered1.png"
        ,"http://bgashare.bingoogolapple.cn/refreshlayout/images/staggered1.png"
        ,"http://bgashare.bingoogolapple.cn/refreshlayout/images/staggered1.png"
        ,"http://bgashare.bingoogolapple.cn/refreshlayout/images/staggered1.png"
        ,"http://bgashare.bingoogolapple.cn/refreshlayout/images/staggered1.png"
        ,"http://bgashare.bingoogolapple.cn/refreshlayout/images/staggered1.png"
        ,"http://bgashare.bingoogolapple.cn/refreshlayout/images/staggered1.png"
        ,"http://bgashare.bingoogolapple.cn/refreshlayout/images/staggered1.png"
        ,"http://bgashare.bingoogolapple.cn/refreshlayout/images/staggered1.png"
        ,"http://bgashare.bingoogolapple.cn/refreshlayout/images/staggered1.png"
    )

    var type: ArrayList<String> = arrayListOf("类型1","类型2","类型3")

    var obj1:CommentItem = CommentItem()

    var obj2:CommentItem = CommentItem()

    var obj3:CommentItem = CommentItem()

    init{
        obj1.name = "会飞的鱼"
        obj1.content = "测试测测试测试测试测试测试测试测试测试测测试测测试测试测试测试测试测"

        obj2.name = "会飞的鱼"
        obj3.content = "测试测测试测试测试测试测试测试测试测试测测试测测试测试测试测试测试测"

        obj3.name = "会飞的鱼"
        obj3.content = "测试测测试测试测"
    }

    var comments: ArrayList<CommentItem> = arrayListOf(obj1,obj2,obj3)


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