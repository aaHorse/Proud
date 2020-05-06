package com.horse.proud.event

import androidx.lifecycle.ViewModel

/**
 * 销毁活动的事件信息
 *
 * @author liliyuan
 * @since 2020年4月8日19:33:17
 * */
class FinishActivityEvent : MessageEvent() {

    var category:Int = -1

}