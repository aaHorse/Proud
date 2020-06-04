package com.horse.core.proud.extension

import android.annotation.SuppressLint
import android.os.Looper
import android.widget.Toast
import com.horse.core.proud.Proud

/**
 * 定义全局的拓展工具
 *
 * @author liliyuan
 * @since 2020年4月7日16:00:58
 * */

private var toast: Toast? = null

/**
 * 该方法仅在主线程调用有效，在非主线程调用将会不显示
 * */
@SuppressLint("ShowToast")
@JvmOverloads
fun showToast(content: String, duration: Int = Toast.LENGTH_SHORT){
    if(Looper.myLooper() == Looper.getMainLooper()){
        if(toast == null){
            toast = Toast.makeText(com.horse.core.proud.Proud.context,content,duration)
        }else{
            toast?.setText(content)
        }
        toast?.show()
    }
}

/**
 * 切换到主线程，弹出Toast
 * */
@SuppressLint("ShowToast")
@JvmOverloads
fun showToastOnUiThread(content: String, duration: Int = Toast.LENGTH_SHORT) {
    Proud.handler.post {
        if (toast == null){
            toast = Toast.makeText(com.horse.core.proud.Proud.context, content, duration)
        }else{
            toast?.setText(content)
        }
        toast?.show()
    }
}