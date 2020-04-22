package com.horse.proud.util

import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToastOnUiThread
import com.horse.core.proud.util.GlobalUtil
import com.horse.network.exception.ResponseCodeException
import com.horse.network.model.Response
import com.horse.proud.R
import com.horse.proud.data.event.ForceToLoginEvent
import org.greenrobot.eventbus.EventBus
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException

/**
 * 对服务器的返回进行初步处理。
 *
 * @author liliyuan
 * @since 2020年4月8日19:57:59
 * */
object ResponseHandler {

    private val TAG = "ResponseHandler"

    /**
     * 当网络请求正常相应时，根据状态码处理通用部分的逻辑。
     *
     * @param response
     * 相应实体类
     *
     * @return 如果在这里处理完了该相应，返回true，否则返回false。
     * */
    fun handleResponse(response: Response?): Boolean{
        if (response == null) {
            logWarn(TAG, "handleResponse: response is null")
            showToastOnUiThread(GlobalUtil.getString(R.string.unknown_error))
            return true
        }
        val status = response.status
        when(status){
            10001,10002,10003 ->{
                logWarn(TAG,"handlePesponse: status code is $status")
                Proud.logout()
                showToastOnUiThread(GlobalUtil.getString(R.string.login_status_expired))
                val event = ForceToLoginEvent()
                /*
                * 发送事件
                * */
                EventBus.getDefault().post(event)
                return true
            }
            19000 ->{
                logWarn(TAG,"handleTesponse: status code is 19000")
                showToastOnUiThread(GlobalUtil.getString(R.string.unknown_error))
                return true
            }
            else ->{
                /*
                * 状态码正常
                * */
                return false
            }
        }
    }

    /**
     * 网络请求没有正常相应时，输出异常信息
     * */
    fun handleFailure(e: Exception){
        when(e){
            is ConnectException -> showToastOnUiThread(GlobalUtil.getString(R.string.network_connect_error))
            is SocketTimeoutException -> showToastOnUiThread(GlobalUtil.getString(R.string.network_connect_timeout))
            is ResponseCodeException -> showToastOnUiThread(GlobalUtil.getString(R.string.network_response_code_error) + e.responseCode)
            is NoRouteToHostException -> showToastOnUiThread(GlobalUtil.getString(R.string.no_route_to_host))
            else ->{
                logWarn(TAG, "handleFailure exception is $e")
                showToastOnUiThread(GlobalUtil.getString(R.string.unknown_error))
            }
        }
    }

}