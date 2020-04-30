package com.horse.proud.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.horse.proud.data.TaskRepository
import com.horse.proud.ui.home.MainActivity

/**
 * @author liliyuan
 * @since 2020年4月25日09:28:25
 * */
class TaskFragmentViewModelFactory(private val repository: TaskRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TaskFragmentViewModel(repository) as T
    }

}