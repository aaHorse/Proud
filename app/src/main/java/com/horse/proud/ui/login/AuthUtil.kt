package com.horse.proud.ui.login

import com.horse.core.proud.Const
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.util.GlobalUtil
import com.horse.core.proud.util.SharedUtil
import com.horse.proud.network.model.base.Callback
import com.horse.proud.network.model.GetBaseinfo
import com.horse.proud.network.model.base.Response
import com.horse.proud.R
import com.horse.core.proud.model.login.Login
import com.horse.core.proud.model.regist.Register
import com.horse.proud.ui.common.BaseActivity
import com.horse.proud.util.ResponseHandler
import com.horse.proud.util.UserUtil

/**
 * 登录和注册的基类。
 *
 * @author liliyuan
 * @since 2020年4月10日05:31:21
 * */
object AuthUtil{

    /**
     * 存储用户的身份信息。
     *
     * @param
     * */
    fun saveAuthData(register:Register){
        with(register){
            SharedUtil.save(Const.Auth.USER_ID,id)
            SharedUtil.save(Const.Auth.NUMBER,number)
            SharedUtil.save(Const.Auth.NAME,name)
            SharedUtil.save(Const.Auth.PHONE,phoneNumber)
            SharedUtil.save(Const.Auth.HEAD,head)
            SharedUtil.save(Const.Auth.INFO,info)
            SharedUtil.save(Const.Auth.TOKEN,token)
            Proud.refreshUserMsg()
        }
    }

    /**
     * 存储用户的登录状态。
     * */
    fun saveAuthState(state:Int){
        SharedUtil.save(Const.Auth.LOGIN_TYPE,state)
        Proud.refreshUserMsg()
    }

    private const val TAG = "AuthActivity"

}