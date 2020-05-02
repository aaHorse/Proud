package com.horse.proud.data

import com.horse.proud.data.network.LoginNetwork
import com.horse.proud.data.network.LostNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class LoginRepository private constructor(private val network: LoginNetwork){

    suspend fun login(username:String,password:String,repassword:String) = withContext(Dispatchers.IO){
        network.fetchLogin(username,password,repassword)
    }

    companion object{

        private lateinit var instance: LoginRepository

        fun getInstance(network: LoginNetwork): LoginRepository {
            if(!Companion::instance.isInitialized){
                synchronized(LostRepository::class.java){
                    if(!Companion::instance.isInitialized){
                        instance = LoginRepository(network)
                    }
                }
            }
            return instance
        }

    }

}