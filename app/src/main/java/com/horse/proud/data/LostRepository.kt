package com.horse.proud.data

import com.horse.core.proud.model.lost.LostItem
import com.horse.core.proud.model.other.CommentItem
import com.horse.proud.data.network.LostNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.ArrayList

/**
 * 失物招领功能
 *
 * @author liliyuan
 * @since 2020年4月26日15:20:59
 * */
class LostRepository private constructor(private val network: LostNetwork){

    suspend fun getLostList() = withContext(Dispatchers.IO){
        network.fetchLostAll()
    }

    suspend fun publish(item: LostItem) = withContext(Dispatchers.IO){
        network.fetchLostPublish(item)
    }

    suspend fun upLoadImage(part: MultipartBody.Part, requestBody: RequestBody) = withContext(Dispatchers.IO){
        network.fetchLostUpLoadImage(part,requestBody)
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

    suspend fun userLost(id:Int) = withContext(Dispatchers.IO){
        network.fetchUserLost(id)
    }

    suspend fun update(item: LostItem) = withContext(Dispatchers.IO){
        network.fetchUpdate(item)
    }

    suspend fun delete(item: LostItem) = withContext(Dispatchers.IO){
        network.fetchDelete(item)
    }

    companion object{

        private lateinit var instance: LostRepository

        fun getInstance(network: LostNetwork):LostRepository{
            if(!::instance.isInitialized){
                synchronized(LostRepository::class.java){
                    if(!::instance.isInitialized){
                        instance = LostRepository(network)
                    }
                }
            }
            return instance
        }

    }

}