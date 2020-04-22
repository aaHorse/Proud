package com.horse.proud

import android.app.Application
import com.horse.core.proud.Proud
import com.horse.proud.di.appModule
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager
import com.umeng.commonsdk.UMConfigure
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        //QMUI 的手势返回需要注册 ActivityLifecycleCallbacks，在这里初始化
        QMUISwipeBackActivityManager.init(this)

        /*
        * 初始化组件化基础库, 所有友盟业务SDK都必须调用此初始化接口
        *
        * 每台设备仅记录首次安装激活的渠道，在其他渠道再次安装不会重复计量
        * */
        UMConfigure.init(this, "5c18feb8f1f5568cb70001f9", "Test", UMConfigure.DEVICE_TYPE_PHONE, null)

        //
        Proud.initialize(this)

        //开始启动koin
        startKoin {
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }
}