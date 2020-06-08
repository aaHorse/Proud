package com.horse.core.proud.util

/**
 * 按钮短时间内重复点击监听
 *
 * @author liliyuan
 * @since 2020年6月8日10:24:36
 * */
object FastClickCheck {

    private const val FAST_CLICK_DELAY_TIME = 3000

    private var lastClickTime:Long = 0

    fun isNotFastClick():Boolean{
        var flag:Boolean = false
        val currentClickTime = System.currentTimeMillis()
        if ((currentClickTime - lastClickTime) >= FAST_CLICK_DELAY_TIME ) {
            flag = true
        }
        lastClickTime = currentClickTime
        return flag
    }

}