package com.horse.proud.event

/**
 * 删除发布任务事件
 *
 * @author liliyuan
 * @since 2020年6月6日09:23:46
 * */
class DeleteEvent:MessageEvent() {

    var category:Int = -1

    /**
     * item对应id
     * */
    lateinit var id:String

}