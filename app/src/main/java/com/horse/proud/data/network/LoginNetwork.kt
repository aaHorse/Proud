package com.horse.proud.data.network

import com.horse.proud.data.model.login.Token
import com.horse.proud.data.model.login.WordsResult
import com.horse.proud.data.model.regist.Register
import com.horse.proud.data.network.api.LoginService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 注册和登录放在了一起
 *
 * @author liliyuan
 * @since 2020年5月2日16:14:51
 * */
class LoginNetwork {

    private val service = ServiceCreator.create(LoginService::class.java)

    suspend fun fetchLogin(number:String,password:String) = service.login(number,password).await()

    suspend fun fetchRegister(register: Register) = service.register(register).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }
            })
        }
    }

    companion object {

        private var network: LoginNetwork? = null

        fun getInstance(): LoginNetwork {
            if (network == null) {
                synchronized(LoginNetwork::class.java) {
                    if (network == null) {
                        network = LoginNetwork()
                    }
                }
            }
            return network!!
        }

    }
}