package com.horse.proud.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.horse.proud.data.TaskRepository

/**
 * @author liliyuan
 * @since 2020年5月4日17:16:46
 * */
class TaskActivityViewModelFactory(private val repository: TaskRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TaskActivityViewModel(repository) as T
    }

}