package com.horse.proud.data

import com.horse.proud.data.model.task.TaskItem
import com.horse.proud.data.network.TaskNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 任务功能
 *
 * @author liliyuan
 * @since 2020年4月26日12:50:27
 * */
class TaskRepository private constructor(private val network: TaskNetwork){

    suspend fun getTaskList() = withContext(Dispatchers.IO){
        network.fetchTaskAll()
    }

    suspend fun publish(task: TaskItem) = withContext(Dispatchers.IO){
        network.fetchTaskPublish(task)
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