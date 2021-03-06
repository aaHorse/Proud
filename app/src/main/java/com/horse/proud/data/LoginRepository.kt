package com.horse.proud.data

import com.horse.core.proud.model.login.Token
import com.horse.core.proud.model.regist.Register
import com.horse.proud.data.network.LoginNetwork
import com.horse.proud.data.network.LostNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 登录和注册的接口放在了一起处理
 *
 * @author liliyuan
 * @since 2020年5月2日19:42:42
 * */
class LoginRepository private constructor(private val network: LoginNetwork){

    suspend fun login(username:String,password:String) = withContext(Dispatchers.IO){
        network.fetchLogin(username,password)
    }

    suspend fun register(register: Register) = withContext(Dispatchers.IO){
        network.fetchRegister(register)
    }

    suspend fun forGetPassword(number:String,phoneNumber:String,newPassword:String) = withContext(Dispatchers.IO){
        network.fetchForGetPassword(number,phoneNumber,newPassword)
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