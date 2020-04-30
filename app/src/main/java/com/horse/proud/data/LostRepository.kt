package com.horse.proud.data

import com.horse.proud.data.network.LostNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 失物招领功能
 *
 * @author liliyuan
 * @since 2020年4月26日15:20:59
 * */
class LostRepository private constructor(private val network: LostNetwork){

    suspend fun getTask() = withContext(Dispatchers.IO){
        network.fetchLostList()
    }

    companion object{

        private lateinit var instance: LostRepository

        fun getInstance(network: LostNetwork): LostRepository {
            if(!Companion::instance.isInitialized){
                synchronized(LostRepository::class.java){
                    if(!Companion::instance.isInitialized){
                        instance =
                            LostRepository(network)
                    }
                }
            }
            return instance
        }

    }

}