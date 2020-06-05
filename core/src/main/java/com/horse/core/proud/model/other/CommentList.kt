package com.horse.core.proud.model.other

import com.google.gson.annotations.SerializedName
import com.horse.core.proud.model.Response

/**
 * @author liliyuan
 * @since 2020年5月6日11:16:25
 * */
class CommentList : Response() {

    @SerializedName("data")
    var commentList: ArrayList<CommentItem>? = null

}