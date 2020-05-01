package com.horse.proud.data.network

import com.horse.proud.data.network.api.TaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author liliyuan
 * @since 2020年4月26日12:54:17
 * */
class TaskNetwork {

    private val service = ServiceCreator.create(TaskService::class.java)

    suspend fun fetchTaskList() = service.getTask().await()

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

        private var network: TaskNetwork? = null

        fun getInstance(): TaskNetwork {
            if (network == null) {
                synchronized(TaskNetwork::class.java) {
                    if (network == null) {
                        network = TaskNetwork()
                    }
                }
            }
            return network!!
        }

    }

}