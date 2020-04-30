package com.horse.proud.data

import com.horse.proud.data.network.RentalNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 物品租赁功能
 *
 * @author liliyuan
 * @since 2020年4月26日15:21:48
 * */
class RentalRepository private constructor(private val network: RentalNetwork){

    suspend fun getTask() = withContext(Dispatchers.IO){
        network.fetchRentalList()
    }

    companion object{

        private lateinit var instance: RentalRepository

        fun getInstance(network: RentalNetwork): RentalRepository {
            if(!Companion::instance.isInitialized){
                synchronized(RentalRepository::class.java){
                    if(!Companion::instance.isInitialized){
                        instance =
                            RentalRepository(network)
                    }
                }
            }
            return instance
        }

    }

}