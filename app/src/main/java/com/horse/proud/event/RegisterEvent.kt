package com.horse.proud.event

import com.horse.proud.data.model.regist.Register

/**
 * 注册成功后，通知登录界面
 *
 * @author liliyuan
 * @since 2020年5月4日12:19:57
 * */
class RegisterEvent :MessageEvent() {

    /**
     * 标记
     * 0：游客登录
     * 1：验证登录
     * */
    var loginState = 0

    lateinit var register:Register

}