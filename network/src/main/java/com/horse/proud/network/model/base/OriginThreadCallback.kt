package com.horse.proud.network.model.base

/**
 * 网络请求响应的回调接口，回调时保留原来线程进行回调，不切换到主线程
 *
 * @author liliyuan
 * @since 2020年4月8日09:56:18
 * */
interface OriginThreadCallback : Callback {
}

