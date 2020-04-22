package com.horse.proud.data.event

/**
 * 销毁活动的事件信息
 *
 * @author liliyuan
 * @since 2020年4月8日19:33:17
 * */
class FinishActivityEvent : MessageEvent() {

    var activityClass: Class<*>? = null

}