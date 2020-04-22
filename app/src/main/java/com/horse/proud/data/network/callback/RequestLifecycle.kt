package com.horse.proud.data.network.callback

/**
 * 网络请求的生命周期
 *
 * @author liliyuan
 * @since 2020年4月6日19:03:53
 * */
interface RequestLifecycle {

    fun startLoading()

    fun loadFinished()

    fun loadFailed(msg: String?)

}