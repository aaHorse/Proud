package com.horse.proud.event

import android.view.View

/**
 * 滑动事件
 *
 * @author liliyuan
 * @since 2020年5月4日05:55:04
 * */
class ScrollEvent:MessageEvent() {

    lateinit var view:View

    var scrollX = 0

    var scrollY = 0

    var oldScrollX = 0

    var oldScrollY = 0

}