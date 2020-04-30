package com.horse.core.proud.extension

import android.os.Handler
import android.view.View

/**
 * android.os.Handler的扩展类。
 *
 * @author liliyuan
 * @since 2020年4月25日06:27:32
 * */
inline fun Handler.postDelayed(delayMillis: Long, crossinline action: () -> Unit) : Runnable {
    val runnable = Runnable { action() }
    postDelayed(runnable, delayMillis)
    return runnable
}

inline fun View.postDelayed(delayMillis: Long, crossinline action: () -> Unit) : Runnable {
    val runnable = Runnable { action() }
    postDelayed(runnable, delayMillis)
    return runnable
}