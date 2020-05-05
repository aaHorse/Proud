package com.horse.proud.ui.task

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logWarn
import com.horse.proud.data.TaskRepository
import com.horse.proud.data.model.task.TaskItem
import kotlinx.coroutines.launch

class TaskFragmentViewModel(private val repository: TaskRepository) : ViewModel(){

    var flag:Int = 0

    var isLoadingMore = MutableLiveData<Boolean>()

    var loadFailed = MutableLiveData<Int>()

    var isNoMoreData = MutableLiveData<Boolean>()

    var taskItems = ArrayList<TaskItem>()

    var taskItemsChanged = MutableLiveData<Int>()

    fun getTask() {
        launch ({
            var taskList = repository.getTaskList()
            when(taskList.status){
                200 -> {
                    taskItems.clear()
                    for(task in taskList.taskList){
                        taskItems.add(task)
                    }
                    isLoadingMore.value = false
                    taskItemsChanged.value = flag++
                }
                500 -> {
                    loadFailed.value = flag++
                }
            }
        }, {
            logWarn(TAG, it.message, it)
            loadFailed.value = flag++
        })
    }

    fun like(id:String){
        launch({
            repository.like(id)
        },{
            logWarn(TAG, it.message, it)
        })
    }

    private fun launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            error(e)
        }
    }

    companion object {

        private const val TAG = "TaskFragmentViewModel"

    }

}