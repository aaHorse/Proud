package com.horse.proud.event

import com.horse.core.proud.model.other.CommentItem

/**
 * 点击评论区的用户，跳转界面查看用户的发布信息
 *
 * @author liliyuan
 * @since 2020年6月7日15:36:47
 * */
class CommentToOverViewEvent : MessageEvent(){

    var category:Int = -1

    var userId:Int = 0

}