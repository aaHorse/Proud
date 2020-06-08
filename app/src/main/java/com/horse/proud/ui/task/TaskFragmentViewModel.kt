package com.horse.proud.ui.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToast
import com.horse.proud.data.TaskRepository
import com.horse.core.proud.model.Response
import com.horse.core.proud.model.other.CommentItem
import com.horse.core.proud.model.task.TaskItem
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TaskFragmentViewModel(private val repository: TaskRepository) : ViewModel(){

    /**
     * 分页用
     *
     * 第一页是0
     * 第二页是1，以此类推
     * */
    var page = 1

    var isLoadingMore = MutableLiveData<Boolean>()

    /**
     * 服务器错误
     * */
    var loadError = MutableLiveData<Int>()

    /**
     * 网络错误
     * */
    var badNetWork = MutableLiveData<Int>()

    /**
     * 空
     * */
    var noContent = MutableLiveData<Int>()

    var isNoMoreData = MutableLiveData<Boolean>()

    var taskItems = ArrayList<TaskItem>()

    var taskItemsChanged = MutableLiveData<Int>()

    /**
     * 获取任务列表
     *
     * @param flag 标志，0：查看全部
     * @param userID 用户id，查看用户对应的信息
     * */
    fun getTask(flag:Int,userID:Int){
        isLoadingMore.value = true
        if(flag == 0){
            getAllTask()
        }else{
            getUserTask(userID)
        }
    }

    private fun getAllTask() {
        launch ({
            var taskList = repository.getTaskList(page++)
            //加载结束
            isLoadingMore.value = false
            when(taskList.status){
                200 -> {
                    for(item in taskList.taskList){
                        if(item.label.startsWith("*")){
                            item.label = item.label.substring(1,item.label.length)
                            taskItems.add(item)
                        }
                    }
                    if(taskItems.size == 0){
                        //空布局
                        noContent.value = noContent.value?.plus(1)
                    }else{
                        if(taskItems.size < 15){
                            isNoMoreData.value = true
                        }
                        getComments()
                    }
                }
                500 -> {
                    isNoMoreData.value = true
                }
            }
        }, {
            logWarn(TAG, it.message, it)
            isLoadingMore.value = false
            badNetWork.value = badNetWork.value?.plus(1)
        })
    }

    private fun getUserTask(userId:Int){
        launch ({
            val taskList = repository.userTask(userId,page++)
            //加载结束
            isLoadingMore.value = false
            when(taskList.status){
                200 -> {
                    for(item in taskList.taskList){
                        if(item.label.startsWith("*")){
                            item.label = item.label.substring(1,item.label.length)
                            taskItems.add(item)
                        }
                    }
                    if(taskItems.size == 0){
                        //空布局
                        noContent.value = noContent.value?.plus(1)
                    }else{
                        if(taskItems.size < 15){
                            isNoMoreData.value = true
                        }
                        getComments()
                    }
                }
                500 -> {
                    isNoMoreData.value = true
                }
            }
        }, {
            logWarn(TAG, it.message, it)
            isLoadingMore.value = false
            badNetWork.value = badNetWork.value?.plus(1)
        })
    }

    fun publishComment(comment: CommentItem){
        launch({
            var response: Response = repository.publishComment(comment)
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
        launch({
            for(item in taskItems){
                val commentList = repository.getComments(item.id)
                when(commentList.status){
                    200 -> {
                        //将评论列表翻转
                        commentList.commentList?.reverse()
                        item.comments = commentList
                    }
                    500 -> {
                        logWarn(TAG,commentList.msg)
                    }
                }
            }
            isLoadingMore.value = false
            taskItemsChanged.value = taskItemsChanged.value?.plus(1)
        },{
            logWarn(TAG,it)
            isLoadingMore.value = false
        })
    }

    fun like(id:String){
        launch({
            repository.like(id)
        },{
            logWarn(TAG, it.message, it)
        })
    }

    /**
     * 删除用户发布的任务
     *
     * @param id 任务id
     * */
    fun delete(id: String){
        launch({
            repository.delete(id)
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