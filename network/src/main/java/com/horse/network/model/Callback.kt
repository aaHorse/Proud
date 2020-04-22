package com.horse.network.model

/**
 * 网络请求响应接口
 *
 * @author liliyuan
 * @since 2020年4月8日08:07:12
 * */
interface Callback {

    fun onResponse(response: Response)

    fun onFailure(e: Exception)

}