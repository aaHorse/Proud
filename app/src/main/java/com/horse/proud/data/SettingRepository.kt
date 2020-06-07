package com.horse.proud.data

import com.horse.core.proud.model.regist.Register
import com.horse.proud.data.network.SettingNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author liliyuan
 * @since 2020年6月4日19:36:25
 * */
class SettingRepository private constructor(private val network:SettingNetwork) {

    suspend fun update(register: Register) = withContext(Dispatchers.IO){
        network.fetchUpdate(register)
    }

    suspend fun userTask(id:Int) = withContext(Dispatchers.IO){
        network.fetchUserTask(id)
    }

    suspend fun userLost(id:Int) = withContext(Dispatchers.IO){
        network.fetchUserLost(id)
    }

    suspend fun userRental(id:Int) = withContext(Dispatchers.IO){
        network.fetchUserRental(id)
    }

    suspend fun userMsg(id:Int) = withContext(Dispatchers.IO){
        network.fetchUserMsg(id)
    }

    companion object{

        private lateinit var instance:SettingRepository

        fun getInstance(network: SettingNetwork):SettingRepository{
            if(!Companion::instance.isInitialized){
                synchronized(SettingRepository::class.java){
                    if(!Companion::instance.isInitialized){
                        instance = SettingRepository(network)
                    }
                }
            }
            return instance
        }

    }

}