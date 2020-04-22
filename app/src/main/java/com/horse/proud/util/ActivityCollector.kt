package com.horse.proud.util

import android.app.Activity
import com.horse.core.proud.extension.logDebug
import java.lang.ref.WeakReference

/**
 * 活动管理器，用于一键退出程序
 *
 * @author liliyuan
 * @since 2020年4月6日20:06:19
 * */
object ActivityCollector {

    private const val TAG = "ActivityCollector"

    private val activityList = ArrayList<WeakReference<Activity>?>()

    fun size(): Int {
        return activityList.size
    }

    fun add(weakReference: WeakReference<Activity>?){
        activityList.add(weakReference)
    }

    fun remove(weakReference: WeakReference<Activity>?){
        val result = activityList.remove(weakReference)
        com.horse.core.proud.extension.logDebug(
            TAG,
            "remove activity reference $result"
        )
    }

    fun finishAll(){
        if(activityList.isNotEmpty()){
            for(activityWeakReference in activityList){
                val activity = activityWeakReference?.get()
                if(activity != null && !activity.isFinishing){
                    activity.finish()
                }
            }
            activityList.clear()
        }
    }
}