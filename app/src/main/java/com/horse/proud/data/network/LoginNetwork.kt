package com.horse.proud.data.network

import com.horse.proud.data.network.api.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LoginNetwork {

    private val service = ServiceCreator.create(LoginService::class.java)

    //suspend fun fetchLostList() = service.getTask().await()
    suspend fun fetchLogin(username:String,password:String,repassword:String) = service.login(username,password,repassword).await()

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