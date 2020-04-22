package com.horse.network.model

import com.google.gson.annotations.SerializedName
import com.horse.network.request.InitRequest


/**
 * 初始化请求。
 *
 * @author liliyuan
 * @since 2020年4月8日07:46:48
 * */
class Init : Response() {

    /**
     * 基本的url地址头，应当根据返回的url地址头来去组装所有后续的访问接口。
     */
    var base: String = ""

    /**
     * 新的token，重新延长了有效限。只有在初始化时传入了老的token，才会有新的token返回。
     */
    var token: String = ""

    /**
     * 已登录用户的头像。只有在初始化时传入了正确的token，才会有返回此字段。
     */
    var avatar: String = ""

    /**
     * 已登录用户的背景图。只有在初始化时传入了正确的token，才会有返回此字段。
     */
    @SerializedName("bg_image")
    var bgImage: String = ""

    /**
     * 是否存在版本更新。
     */
    @SerializedName("has_new_version")
    var hasNewVersion = false

    /**
     * 版本更新的具体信息。
     */
    var version: Version? = null

    companion object {

        fun getResponse(callback: Callback) {
            InitRequest().listen(callback)
        }
    }

}