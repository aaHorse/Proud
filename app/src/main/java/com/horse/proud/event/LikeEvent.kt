package com.horse.proud.event

/**
 * 点赞事件
 *
 * @author liliyuan
 * @since 2020年5月5日16:19:34
 * */
class LikeEvent:MessageEvent() {

    var category:Int = -1

    /**
     * item对应id
     * */
    lateinit var id:String

}