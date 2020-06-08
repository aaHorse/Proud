package com.horse.proud.data.network

import com.horse.core.proud.model.other.CommentItem
import com.horse.core.proud.model.rental.RentalItem
import com.horse.proud.data.network.api.RentalService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
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

    suspend fun fetchRentalAll(page:Int) = service.getAll(page).await()

    suspend fun fetchRentalUpLoadImage(part: MultipartBody.Part, requestBody: RequestBody) = service.uploadImage(part,requestBody).await()

    suspend fun fetchTaskUpLoadImages(parts: ArrayList<MultipartBody.Part>, requestBody: RequestBody) = service.uploadImages(parts,requestBody).await()

    suspend fun fetchLike(id:String) = service.like(id).await()

    suspend fun fetchGetComments(id:String) = service.getComments(id).await()

    suspend fun fetchPublishComment(comment: CommentItem) = service.publishComment(comment).await()

    suspend fun fetchUserRental(page:Int,id:Int) = service.userRental(page,id).await()

    suspend fun fetchUpdate(task: RentalItem) = service.update(task).await()

    suspend fun fetchDelete(item: RentalItem) = service.delete(item).await()

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