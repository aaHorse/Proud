package com.horse.proud.data.network

import com.horse.core.proud.model.regist.Register
import com.horse.proud.data.network.api.SettingService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author liliyuan
 * @since 2020年6月4日19:30:44
 * */
class SettingNetwork {

    private val service = ServiceCreator.create(SettingService::class.java)

    suspend fun fetchUpdate(register: Register) = service.update(register).await()

    suspend fun fetchUserTask(id:Int) = service.userTask(id).await()

    suspend fun fetchUserLost(id:Int) = service.userLostAndFound(id).await()

    suspend fun fetchUserRental(id:Int) = service.userRental(id).await()

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

        private var network: SettingNetwork? = null

        fun getInstance(): SettingNetwork {
            if (network == null) {
                synchronized(SettingNetwork::class.java) {
                    if (network == null) {
                        network = SettingNetwork()
                    }
                }
            }
            return network!!
        }

    }

}