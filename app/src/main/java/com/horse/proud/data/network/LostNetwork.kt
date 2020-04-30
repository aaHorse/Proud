package com.horse.proud.data.network

import com.horse.proud.data.network.api.LostService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author liliyuan
 * @since 2020年4月26日15:16:59
 * */
class LostNetwork {

    private val service = ServiceCreator.create(LostService::class.java)

    suspend fun fetchLostList() = service.getTask().await()

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

        private var network: LostNetwork? = null

        fun getInstance(): LostNetwork {
            if (network == null) {
                synchronized(LostNetwork::class.java) {
                    if (network == null) {
                        network = LostNetwork()
                    }
                }
            }
            return network!!
        }

    }

}