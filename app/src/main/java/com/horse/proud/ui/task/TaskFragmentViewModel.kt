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

    /**
     * 监听 taskItems
     * */
    var dataChanged = MutableLiveData<Int>()

    var isLoadingMore = MutableLiveData<Boolean>()

    var loadFailed = MutableLiveData<Int>()

    var isNoMoreData = MutableLiveData<Boolean>()

    var taskItems = ArrayList<TaskItem>()

    fun getTask() {
        launch ({
            var taskList = repository.getTaskList()
            for(task in taskList.taskList){
                taskItems.add(task)
            }
            isLoadingMore.value = false
            dataChanged.value = dataChanged.value?.plus(1)
        }, {
            logWarn(TAG, it.message, it)
            loadFailed.value = dataChanged.value?.plus(1)
            Toast.makeText(Proud.getContext(), it.message, Toast.LENGTH_SHORT).show()
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