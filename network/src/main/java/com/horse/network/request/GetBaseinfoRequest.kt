package com.horse.network.request

import com.horse.core.proud.Proud
import com.horse.network.model.Callback
import com.horse.network.model.GetBaseinfo
import com.horse.network.util.NetworkConst
import okhttp3.Headers

/**
 * 获取当前用户的基本信息请求。
 *
 * 对应服务器接口：/user/baseinfo
 *
 * @author liliyuan
 * @since 2020年4月10日06:04:19
 *
 * */
class GetBaseinfoRequest : Request() {

    override fun url(): String{
        return URL
    }

    override fun method(): Int {
        return Request.GET
    }

    override fun listen(callback: Callback?) {
        setListener(callback)
        inFlight(GetBaseinfo::class.java)
    }

    override fun params(): Map<String,String>?{
        val params = HashMap<String,String>()
        return if(buildAuthParams(params)){
            params
        }else{
            super.params()
        }
    }

    override fun headers(builder: Headers.Builder): Headers.Builder {
        buildAuthHeaders(builder, NetworkConst.DEVICE_SERIAL, NetworkConst.TOKEN)
        return super.headers(builder)
    }

    companion object{

        private val URL = Proud.BASE_URL + "/user/baseinfo"

    }

}