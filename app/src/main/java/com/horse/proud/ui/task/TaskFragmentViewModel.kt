package com.horse.proud.ui.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToast
import com.horse.proud.data.TaskRepository
import com.horse.proud.data.model.Response
import com.horse.proud.data.model.other.CommentItem
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
                    for(item in taskList.taskList){
                        taskItems.add(item)
                    }
                    getComments()
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

    fun publishComment(comment:CommentItem){
        launch({
            var response:Response = repository.publishComment(comment)
            when(response.status){
                200 -> {
                    //showToast("成功")
                }
                500 -> {
                    //showToast("失败1")
                }
            }
        },{
            logWarn(TAG,it)
            //showToast("失败2")
        })
    }

    private fun getComments(){
        for((index,item) in taskItems.withIndex()){
            launch({
                logWarn(TAG,item.id)
                val commentList = repository.getComments(item.id)
                when(commentList.status){
                    200 -> {
                        item.comments = commentList
                        logWarn(TAG,"$index")
                        if(index == taskItems.size-1){
                            isLoadingMore.value = false
                            taskItemsChanged.value = flag++
                        }
                    }
                    500 -> {
                        logWarn(TAG,commentList.msg)
                    }
                }
            },{
                logWarn(TAG,it)
            })
        }
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