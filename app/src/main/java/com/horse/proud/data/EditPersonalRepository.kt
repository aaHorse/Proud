package com.horse.proud.data

import com.horse.proud.data.model.regist.Register
import com.horse.proud.data.network.EditPersonalInfoNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author liliyuan
 * @since 2020年6月4日19:36:25
 * */
class EditPersonalRepository private constructor(private val network:EditPersonalInfoNetwork) {

    suspend fun update(register: Register) = withContext(Dispatchers.IO){
        network.fetchUpdate(register)
    }

    companion object{

        private lateinit var instance:EditPersonalRepository

        fun getInstance(network: EditPersonalInfoNetwork):EditPersonalRepository{
            if(!Companion::instance.isInitialized){
                synchronized(EditPersonalRepository::class.java){
                    if(!Companion::instance.isInitialized){
                        instance = EditPersonalRepository(network)
                    }
                }
            }
            return instance
        }

    }

}