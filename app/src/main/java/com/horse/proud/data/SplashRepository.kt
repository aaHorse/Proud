package com.horse.proud.data

import com.horse.proud.data.network.SplashNetWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 启动界面
 *
 * @author liliyuan
 * @since 2020年6月2日15:34:10
 * */
class SplashRepository private constructor(private val network: SplashNetWork){

    suspend fun checkNewVersion(tempVersion:String) = withContext(Dispatchers.IO){
        network.fetchCheckNewVersion(tempVersion)
    }

    companion object{

        private lateinit var instance: SplashRepository

        fun getInstance(network: SplashNetWork): SplashRepository {
            if(!Companion::instance.isInitialized){
                synchronized(SplashRepository::class.java){
                    if(!Companion::instance.isInitialized){
                        instance = SplashRepository(network)
                    }
                }
            }
            return instance
        }

    }

}