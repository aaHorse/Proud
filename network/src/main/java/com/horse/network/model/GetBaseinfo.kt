package com.horse.network.model

import com.google.gson.annotations.SerializedName
import com.horse.network.request.GetBaseinfoRequest

/**
 * 获取当前登录用户基本信息请求的实体类封装。
 *
 * @author liliyuan
 * @since 2020年4月10日06:12:50
 * */
class GetBaseinfo : Response() {

    /**
     * 当前登录用户的名称。
     * */
    var nickname: String = ""

    /**
     * 当前登录用户的头像。
     * */
    var avatar: String = ""

    /**
     * 当前登录用户的个人简介。
     * */
    var description: String = ""

    /**
     * 当前登录用户的个人主页的背景图。
     * */
    @SerializedName("bg_image")
    var bgImage: String = ""

    companion object{

        fun getResponse(callback: Callback){
            GetBaseinfoRequest().listen(callback)
        }

    }

}