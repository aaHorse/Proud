package com.horse.proud.network.request

import com.horse.proud.network.model.FetchTaskItems
import com.horse.proud.network.model.base.Callback
import com.horse.proud.network.request.base.Request
import com.horse.proud.network.util.NetworkConst
import okhttp3.Headers
import java.util.HashMap

/**
 * 获取任务列表的请求。
 *
 * @author liliyuan
 * @since 2020年4月25日18:14:25
 * */
class FetchTaskItemsRequest : Request(){

    private var lastFeed: Long = 0

    fun lastFeed(lastFeed: Long): FetchTaskItemsRequest {
        this.lastFeed = lastFeed
        return this
    }

    override fun url(): String {
        return URL
    }

    override fun method(): Int {
        return Request.GET
    }

    override fun listen(callback: Callback?) {
        setListener(callback)
        inFlight(FetchTaskItems::class.java)
    }

    override fun params(): Map<String, String>? {
        val params = HashMap<String, String>()
        if (buildAuthParams(params)) {
            if (lastFeed > 0) {
                params[NetworkConst.LAST_FEED] = lastFeed.toString()
            }
            return params
        }
        return super.params()
    }

    override fun headers(builder: Headers.Builder): Headers.Builder {
        buildAuthHeaders(builder, NetworkConst.TOKEN, NetworkConst.UID, NetworkConst.DEVICE_SERIAL)
        return super.headers(builder)
    }

    companion object {

        private const val URL = "https://wanandroid.com/wxarticle/chapters/json"

    }
}