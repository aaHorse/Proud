package com.horse.proud.data.network

import com.horse.proud.data.model.regist.Register
import com.horse.proud.data.network.api.EditPersonalInfoService
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
class EditPersonalInfoNetwork {

    private val service = ServiceCreator.create(EditPersonalInfoService::class.java)

    suspend fun fetchUpdate(register: Register) = service.update(register).await()

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

        private var network: EditPersonalInfoNetwork? = null

        fun getInstance(): EditPersonalInfoNetwork {
            if (network == null) {
                synchronized(EditPersonalInfoNetwork::class.java) {
                    if (network == null) {
                        network = EditPersonalInfoNetwork()
                    }
                }
            }
            return network!!
        }

    }

}