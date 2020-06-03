package com.horse.proud.data.network

import com.horse.proud.data.network.api.SplashService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author liliyuan
 * @since 2020年6月2日15:30:10
 * */
class SplashNetWork {

    private val service = ServiceCreator.create(SplashService::class.java)

    suspend fun fetchCheckNewVersion(tempVersion:String) = service.checkNewVersion(tempVersion).await()

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

        private var network: SplashNetWork? = null

        fun getInstance(): SplashNetWork {
            if (network == null) {
                synchronized(SplashNetWork::class.java) {
                    if (network == null) {
                        network = SplashNetWork()
                    }
                }
            }
            return network!!
        }

    }

}