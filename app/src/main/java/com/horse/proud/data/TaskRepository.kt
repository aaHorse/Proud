package com.horse.proud.data

import com.horse.core.proud.model.other.CommentItem
import com.horse.core.proud.model.task.TaskItem
import com.horse.proud.data.network.TaskNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.util.ArrayList

/**
 * 任务功能
 *
 * @author liliyuan
 * @since 2020年4月26日12:50:27
 * */
class TaskRepository private constructor(private val network: TaskNetwork){

    suspend fun getTaskList(page:Int) = withContext(Dispatchers.IO){
        network.fetchTaskAll(page)
    }

    suspend fun publish(task: TaskItem) = withContext(Dispatchers.IO){
        network.fetchTaskPublish(task)
    }

    suspend fun upLoadImage(part: MultipartBody.Part,requestBody: RequestBody) = withContext(Dispatchers.IO){
        network.fetchTaskUpLoadImage(part,requestBody)
    }

    suspend fun upLoadImages(parts: ArrayList<MultipartBody.Part>, requestBody: RequestBody) = withContext(Dispatchers.IO){
        network.fetchTaskUpLoadImages(parts,requestBody)
    }

    suspend fun like(id:String) = withContext(Dispatchers.IO){
        network.fetchLike(id)
    }

    suspend fun getComments(id:String) = withContext(Dispatchers.IO){
        network.fetchGetComments(id)
    }

    suspend fun publishComment(comment: CommentItem) = withContext(Dispatchers.IO){
        network.fetchPublishComment(comment)
    }

    suspend fun userTask(id:Int,page:Int) = withContext(Dispatchers.IO){
        network.fetchUserTask(id,page)
    }

    suspend fun update(task: TaskItem) = withContext(Dispatchers.IO){
        network.fetchUpdate(task)
    }

    suspend fun delete(id:String) = withContext(Dispatchers.IO){
        network.fetchDelete(id)
    }

    companion object{

        private lateinit var instance: TaskRepository

        fun getInstance(network: TaskNetwork):TaskRepository{
            if(!::instance.isInitialized){
                synchronized(TaskRepository::class.java){
                    if(!::instance.isInitialized){
                        instance = TaskRepository(network)
                    }
                }
            }
            return instance
        }

    }

}