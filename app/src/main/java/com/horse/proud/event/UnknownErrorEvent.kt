package com.horse.proud.event

import com.horse.proud.network.model.base.Response

/**
 * 发生未知错误。
 *
 * @author liliyuan
 * @since 2020年4月25日18:44:52
 * */
class UnknownErrorEvent : MessageEvent() {

    var response:Response ?= null

}