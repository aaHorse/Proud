package com.horse.proud.data.model.other

import com.google.gson.annotations.SerializedName
import com.horse.proud.data.model.Response

/**
 * @author liliyuan
 * @since 2020年5月6日11:16:25
 * */
class CommentList : Response() {

    @SerializedName("data")
    var commentList: ArrayList<CommentItem>? = null

}