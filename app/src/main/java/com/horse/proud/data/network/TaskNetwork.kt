package com.horse.proud.data.network

import com.horse.core.proud.model.other.CommentItem
import com.horse.core.proud.model.task.TaskItem
import com.horse.proud.data.network.api.TaskService
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
 * @since 2020年4月26日12:54:17
 * */
class TaskNetwork {

    private val service = ServiceCreator.create(TaskService::class.java)

    suspend fun fetchTaskPublish(task: TaskItem) = service.publish(task).await()

    suspend fun fetchTaskAll(page:Int) = service.getAll(page).await()

    suspend fun fetchTaskUpLoadImage(part: MultipartBody.Part,requestBody: RequestBody) = service.uploadImage(part,requestBody).await()

    suspend fun fetchTaskUpLoadImages(parts: ArrayList<MultipartBody.Part>, requestBody: RequestBody) = service.uploadImages(parts,requestBody).await()

    suspend fun fetchLike(id:String) = service.like(id).await()

    suspend fun fetchGetComments(id:String) = service.getComments(id).await()

    suspend fun fetchPublishComment(comment: CommentItem) = service.publishComment(comment).await()

    suspend fun fetchUserTask(id:Int,page:Int) = service.userTask(id,page).await()

    suspend fun fetchUpdate(task: TaskItem) = service.update(task).await()

    suspend fun fetchDelete(id:String) = service.delete(id).await()

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