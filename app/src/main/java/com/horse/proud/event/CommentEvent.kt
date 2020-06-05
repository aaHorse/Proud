package com.horse.proud.event

import com.horse.core.proud.model.other.CommentItem

/**
 * 发表评论事件
 *
 * @author liliyuan
 * @since 2020年5月6日16:12:22
 * */
class CommentEvent : MessageEvent(){

    var category:Int = -1

    lateinit var comment: CommentItem

}