package com.horse.proud.ui.login.ui

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
import com.horse.proud.ui.common.BaseActivity
import com.horse.proud.util.ResponseHandler
import com.horse.proud.util.UserUtil

/**
 * 登录和注册的基类。
 *
 * @author liliyuan
 * @since 2020年4月10日05:31:21
 * */
abstract class AuthActivity : BaseActivity(){

    /**
     * 根据参数中传入的登录类型数值获取登录类型的名称。
     *
     * @param loginType
     * 登录类型的数值
     *
     * @return 登录类型的名称。
     * */
    protected fun getLoginTypeName(loginType: Int) = when(loginType){
        TYPE_QQ_LOGIN -> "QQ"
        TYPE_WECHAT_LOGIN -> "微信"
        TYPE_WEIBO_LOGIN -> "微博"
        TYPE_GUEST_LOGIN -> "游客"
        else -> ""
    }

    /**
     * 存储用户的身份信息。
     *
     * @param
     * */
    protected fun saveAuthData(userId: Long, token: String, loginType: Int){
        SharedUtil.save(Const.Auth.USER_ID, userId)
        SharedUtil.save(Const.Auth.TOKEN,token)
        SharedUtil.save(Const.Auth.LOGIN_TYPE,loginType)
        Proud.refreshLoginState()
    }

    /**
     * 获取当前登录用户的基本信息。
     * */
    protected fun getUserBaseinfo(){
        GetBaseinfo.getResponse(object: Callback {
            override fun onResponse(response: Response) {
                if(activity == null){
                    return
                }
                if(!ResponseHandler.handleResponse(response)){
                    val baseinfo = response as GetBaseinfo
                    val status = baseinfo.status
                    when(status){
                        0 -> {
                            UserUtil.saveNickname(baseinfo.nickname)
                            UserUtil.saveAvatar(baseinfo.avatar)
                            UserUtil.saveDescription(baseinfo.description)
                            UserUtil.saveBgImage(baseinfo.bgImage)
                            forwardToMainActivity()
                        }
                        10202 -> {
                            showToast(GlobalUtil.getString(R.string.get_baseinfo_failed_user_not_exist))
                            Proud.logout()
                            finish()
                        }
                        else -> {
                            logWarn(TAG,"Get user baseinfo failed." + GlobalUtil.getResponseClue(status, baseinfo.msg))
                            showToast(GlobalUtil.getString(R.string.get_baseinfo_failed))
                            Proud.logout()
                            finish()
                        }
                    }
                }else{
                    activity?.let{
                        /*
                        * 待修改。
                        *
                        * 2020年4月10日06:52:28
                        * */
                        if(it.javaClass.name == "club.giffun.app.LoginDialogActivity"){
                            finish()
                        }
                    }
                }
            }

            override fun onFailure(e: Exception) {
                logWarn(TAG,e.message,e)
                showToast(GlobalUtil.getString(R.string.get_baseinfo_failed))
                Proud.logout()
                finish()
            }

        })
    }

    protected abstract fun forwardToMainActivity()

    companion object {

        private const val TAG = "AuthActivity"

        /**
         * QQ第三方登录的类型。
         */
        const val TYPE_QQ_LOGIN = 1

        /**
         * 微信第三方登录的类型。
         */
        const val TYPE_WECHAT_LOGIN = 2

        /**
         * 微博第三方登录的类型。
         */
        const val TYPE_WEIBO_LOGIN = 3

        /**
         * 手机号登录的类型。
         */
        const val TYPE_PHONE_LOGIN = 4

        /**
         * 游客登录的类型，此登录只在测试环境下有效，线上环境没有此项功能。
         */
        const val TYPE_GUEST_LOGIN = 0
    }

}