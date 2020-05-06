package com.horse.proud.ui.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horse.core.proud.Const
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logError
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.R
import com.horse.proud.data.TaskRepository
import com.horse.proud.data.model.task.TaskItem
import com.horse.proud.event.FinishActivityEvent
import com.horse.proud.util.DateUtil
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import java.io.File


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

    var imagePath:String? = null

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
            task.startTime = DateUtil.nowDateTime
            task.endTime = time
            task.thumbUp = 0
            task.collect = 0
            task.comment = 0

            val response = repository.publish(task)

            when(response.status){
                200 ->{
                    upLoadImage(response.data)
                    logWarn(TAG,response.data)
                    logWarn(TAG,imagePath)
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

    private fun upLoadImage(id:String){
        launch({
            imagePath?.let {
                logWarn(TAG,it)
                val file = File(it)
                val requestFile = RequestBody.create(MediaType.parse("multipart/jpg"), file)

                val part = MultipartBody.Part.createFormData("file", file.name, requestFile)

                val requestBody: RequestBody = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    id
                )

                val response = repository.upLoadImage(part,requestBody)
                when(response.status){
                    200 ->{
                        showToast("任务发布成功")
                        val finishActivityEvent = FinishActivityEvent()
                        finishActivityEvent.category = Const.Like.TASK
                        EventBus.getDefault().post(finishActivityEvent)
                    }
                    500 ->{
                        showToast("任务发布失败")
                    }
                }
            }
        },{
            showToast("图片上传失败")
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