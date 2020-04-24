package com.horse.proud.network.request

import com.horse.core.proud.Proud
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.network.model.base.Callback
import com.horse.proud.network.model.Init
import com.horse.proud.network.request.base.Request
import com.horse.proud.network.util.NetworkConst
import okhttp3.Headers

/**
 * 初始化请求。
 *
 * 对应接口：/init
 *
 * @author liliyuan
 * @since 2020年4月8日19:15:22
 * */
class InitRequest : Request() {

    init {
        connectTimeout(5)
        readTimeout(5)
        writeTimeout(5)
    }

    override fun listen(callback: Callback?){
        setListener(callback)
        inFlight(Init::class.java)
    }

    override fun url(): String {
        return URL
    }

    override fun method(): Int {
        return Request.GET
    }

    override fun params():Map<String,String>?{
        val params = HashMap<String,String>()
        params[NetworkConst.CLIENT_VERSION] = GlobalUtil.appVersionCode.toString()
        val appChannel =  GlobalUtil.getApplicationMetaData("APP_CHANNEL")
        if (appChannel != null) {
            params[NetworkConst.CLIENT_CHANNEL] = appChannel
        }
        if (buildAuthParams(params)) {
            params[NetworkConst.DEVICE_NAME] = deviceName
        }
        return params
    }

    override fun headers(builder: Headers.Builder): Headers.Builder {
        buildAuthHeaders(builder, NetworkConst.UID, NetworkConst.TOKEN)
        return super.headers(builder)
    }

    companion object {

        private val URL = Proud.BASE_URL + "/init"

    }

}