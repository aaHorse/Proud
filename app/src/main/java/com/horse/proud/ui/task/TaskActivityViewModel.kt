package com.horse.proud.ui.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logError
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.R
import com.horse.proud.data.TaskRepository
import com.horse.proud.data.model.task.TaskItem
import kotlinx.coroutines.launch

/**
 * 发布任务
 *
 * @author liliyuan
 * @since 2020年5月4日13:54:14
 * */
class TaskActivityViewModel(private val repository: TaskRepository) : ViewModel() {

    var content = MutableLiveData<String>()

    var local:String = ""

    var type:String = ""

    var time:String = ""

    fun publish(){
        launch({
            var task = TaskItem()
            task.userId = Proud.getUserId()
            logWarn(TAG,"${task.userId}")
            task.title = "会飞的鱼"
            task.content = content.value.toString()
            task.label = type
            task.location = local
            task.image = ""
            task.done = 0
            task.startTime = "2020年5月4日18:36:54"
            task.endTime = time
            task.thumbUp = 0
            task.collect = 0
            task.comment = 0

            val response = repository.publish(task)

            when(response.status){
                200 ->{
                    showToast("任务发布成功")
                }
                500 ->{
                    showToast("任务发布失败")
                }
            }

        },{
            showToast(GlobalUtil.getString(R.string.unknown_error))
            logError(TAG,it)
        })
    }

    private fun launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            error(e)
        }
    }

    companion object{

        private const val TAG = "TaskActivityViewModel"

    }

}