package com.horse.network.model

/**
 * 请求结果的基类
 *
 * @author liliyuan
 * @since 2020年4月8日07:49:26
 * */
open class Response {

    /**
     * 请求结果状态码
     * */
    var status: Int = 0

    /**
     * 请求结果的简单描述
     * */
    var msg: String = ""

}