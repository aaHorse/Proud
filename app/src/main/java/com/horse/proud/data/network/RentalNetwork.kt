package com.horse.proud.data.network

import com.horse.proud.data.model.rental.RentalItem
import com.horse.proud.data.network.api.RentalService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author liliyuan
 * @since 2020年4月26日15:18:46
 * */
class RentalNetwork {

    private val service = ServiceCreator.create(RentalService::class.java)

    suspend fun fetchRentalPublish(item: RentalItem) = service.publish(item).await()

    suspend fun fetchRentalAll() = service.getAll().await()

    suspend fun fetchRentalUpLoadImage(part: MultipartBody.Part, requestBody: RequestBody) = service.uploadImage(part,requestBody).await()

    suspend fun fetchLike(id:String) = service.like(id).await()

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

        private var network: RentalNetwork? = null

        fun getInstance(): RentalNetwork {
            if (network == null) {
                synchronized(RentalNetwork::class.java) {
                    if (network == null) {
                        network = RentalNetwork()
                    }
                }
            }
            return network!!
        }

    }

}