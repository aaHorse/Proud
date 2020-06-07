package com.horse.core.proud.model.other

import com.google.gson.annotations.SerializedName

/**
 * 评论
 *
 * @author liliyuan
 * @since 2020年4月30日19:41:40
 * */
class CommentItem {

    var id:String = ""

    @SerializedName("user_id")
    var userId:Int = -1

    var content:String = ""

    @SerializedName("publish_time")
    var time:String = ""

    /**
     * 条目对应 id
     */
    @SerializedName("table_id")
    var itemId:String = ""

    @SerializedName("thumb_up")
    var thumbUp:Int = 0

    var comment:Int = 0

    /**
     * 姓名
     * */
    var name:String = ""

    /**
     * 学号
     * */
    var accountNumber = ""

}