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
import com.horse.core.proud.model.task.TaskItem
import com.horse.proud.event.FinishActivityEvent
import com.horse.proud.util.DateUtil
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import retrofit2.http.Multipart
import java.io.File
import java.util.ArrayList


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

    var imagesPath = ArrayList<String>()

    fun publish(){
        launch({
            val task = TaskItem()
            task.userId = Proud.register.id
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
                    when {
                        imagesPath.isNullOrEmpty() -> {
                            showToast("任务发布成功")
                            val finishActivityEvent = FinishActivityEvent()
                            finishActivityEvent.category = Const.Like.TASK
                            EventBus.getDefault().post(finishActivityEvent)
                        }
                        imagesPath.size == 1 -> {
                            upLoadImage(response.data,imagesPath[0])
                        }
                        imagesPath.size > 1 -> {
                            upLoadImages(response.data,imagesPath)
                        }
                    }

                    logWarn(TAG,response.data)
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

    private fun upLoadImage(id:String,imagePath:String){
        launch({
            if(imagePath.isNotEmpty()){
                val file = File(imagePath)
                val requestFile = RequestBody.create(MediaType.parse("multipart/jpg"), file)

                val part = MultipartBody.Part.createFormData("file", file.name, requestFile)

                val requestBody: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), id)

                val response = repository.upLoadImage(part,requestBody)
                when(response.code){
                    "1000" ->{
                        showToast("任务发布成功")
                        val finishActivityEvent = FinishActivityEvent()
                        finishActivityEvent.category = Const.Like.TASK
                        EventBus.getDefault().post(finishActivityEvent)
                    }
                }
            }
        },{
            showToast("图片上传失败")
            logError(TAG,it)
        })
    }

    private fun upLoadImages(id:String,imagesPath:ArrayList<String>){
        launch({
            if(!imagesPath.isNullOrEmpty()){
                val list = ArrayList<MultipartBody.Part>()
                for(i in 0 until imagesPath.size){
                    val file = File(imagesPath[i])
                    val requestFile = RequestBody.create(MediaType.parse("multipart/jpg"), file)
                    val part = MultipartBody.Part.createFormData("files", file.name, requestFile)
                    list.add(part)
                }
                val requestBody: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), id)
                val response = repository.upLoadImages(list,requestBody)
                when(response.code){
                    "1000" ->{
                        showToast("任务发布成功")
                        val finishActivityEvent = FinishActivityEvent()
                        finishActivityEvent.category = Const.Like.TASK
                        EventBus.getDefault().post(finishActivityEvent)
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