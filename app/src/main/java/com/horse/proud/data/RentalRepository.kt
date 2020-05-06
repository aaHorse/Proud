package com.horse.proud.data

import com.horse.proud.data.model.other.CommentItem
import com.horse.proud.data.model.rental.RentalItem
import com.horse.proud.data.network.RentalNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * 物品租赁功能
 *
 * @author liliyuan
 * @since 2020年4月26日15:21:48
 * */
class RentalRepository private constructor(private val network: RentalNetwork){

    suspend fun getRentalList() = withContext(Dispatchers.IO){
        network.fetchRentalAll()
    }

    suspend fun publish(item: RentalItem) = withContext(Dispatchers.IO){
        network.fetchRentalPublish(item)
    }

    suspend fun upLoadImage(part: MultipartBody.Part, requestBody: RequestBody) = withContext(Dispatchers.IO){
        network.fetchRentalUpLoadImage(part,requestBody)
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

    companion object{

        private lateinit var instance: RentalRepository

        fun getInstance(network: RentalNetwork):RentalRepository{
            if(!::instance.isInitialized){
                synchronized(RentalRepository::class.java){
                    if(!::instance.isInitialized){
                        instance = RentalRepository(network)
                    }
                }
            }
            return instance
        }

    }

}