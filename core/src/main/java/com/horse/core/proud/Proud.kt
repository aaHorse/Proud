package com.horse.core.proud

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.horse.core.proud.util.SharedUtil

/**
 * 全局API接口
 *
 * @author liliyuan
 * @since 2020年4月7日16:19:13
 */
object Proud {
    var isDebug = false

    @JvmStatic
    lateinit var context:Context

    /**
     * 获取创建在主线程上的Handler对象。
     *
     * @return 创建在主线程上的Handler对象。
     */
    @JvmStatic
    lateinit var handler: Handler

    /**
     * 是否在登录状态
     * */
    var loginState = Const.Auth.UNCHECK

    var register:Register? = null


    var BASE_URL =
        if (isDebug) "http://192.168.31.177:3000" else "http://api.quxianggif.com"

    /**
     * 初始化。需要在程序初始化时，调用本方法进行初始化。
     *
     * @param c Application的Context。注意：不是Activity的context
     */
    fun initialize(c: Context) {
        context = c
        handler = Handler(Looper.getMainLooper())
        refreshUserMsg()
    }

    val packageName: String
        get() = context.packageName

    /**
     * 注销用户登录。
     */
    fun logout() {
        SharedUtil.clear(Const.Auth.LOGIN_TYPE)
        SharedUtil.clear(Const.Auth.USER_ID)
        SharedUtil.clear(Const.Auth.NUMBER)
        SharedUtil.clear(Const.Auth.NAME)
        SharedUtil.clear(Const.Auth.PHONE)
        SharedUtil.clear(Const.Auth.HEAD)
        SharedUtil.clear(Const.Auth.INFO)
        SharedUtil.clear(Const.Auth.TOKEN)
        refreshUserMsg()
    }

    /**
     * 刷新用户的信息。
     */
    fun refreshUserMsg() {
        userId = SharedUtil.read(Const.Auth.USER_ID,0)
        accountNumber = SharedUtil.read(Const.Auth.NUMBER,"NULL")
        name = SharedUtil.read(Const.Auth.NAME,"NULL")
        phone = SharedUtil.read(Const.Auth.PHONE,"NULL")
        head =  SharedUtil.read(Const.Auth.HEAD,"NULL")
        info = SharedUtil.read(Const.Auth.INFO,"NULL")
        token = SharedUtil.read(Const.Auth.TOKEN,"NULL")
        refreshLoginState()
    }

    /**
     * 刷新用户的登录状态。
     *
     * -1:用户未选择身份
     * 0:游客
     * 1：正常用户
     * */
    private fun refreshLoginState(){
        loginState = SharedUtil.read(Const.Auth.LOGIN_TYPE,Const.Auth.UNCHECK)
    }

}