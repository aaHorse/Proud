package com.horse.proud.ui.rental

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
import com.horse.proud.data.RentalRepository
import com.horse.core.proud.model.rental.RentalItem
import com.horse.proud.event.FinishActivityEvent
import com.horse.proud.util.DateUtil
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.util.ArrayList

/**
 * @author liliyuan
 * @since 2020年5月6日07:36:46
 * */
class RentalActivityViewModel(private val repository: RentalRepository) : ViewModel() {

    var content = MutableLiveData<String>()

    var local:String = ""

    var type:String = ""

    var time:String = ""

    var imagesPath = ArrayList<String>()

    var done:Boolean = false

    fun publish(){
        launch({
            val item = RentalItem()
            item.userId = Proud.register.id
            logWarn(TAG,"${item.userId}")
            item.title = Proud.register.name
            item.content = content.value.toString()
            item.label = "*$type"
            item.location = local
            item.image = ""
            item.done = 0
            item.startTime = DateUtil.nowDateTime

            if(time.isEmpty()){
                item.endTime = "不过期"
            }else{
                item.endTime = time
            }

            item.thumbUp = 0
            item.collect = 0
            item.comment = 0

            val response = repository.publish(item)

            when(response.status){
                200 ->{
                    when {
                        imagesPath.isNullOrEmpty() -> {
                            showToast("发布成功")
                            val finishActivityEvent = FinishActivityEvent()
                            finishActivityEvent.category = Const.Like.RENTAL
                            EventBus.getDefault().post(finishActivityEvent)
                        }
                        imagesPath.size == 1 -> {
                            upLoadImage(response.data,imagesPath[0])
                        }
                        imagesPath.size > 1 -> {
                            upLoadImages(response.data,imagesPath)
                        }
                    }
                }
                500 ->{
                    showToast("发布失败")
                }
            }

        },{
            showToast(GlobalUtil.getString(R.string.unknown_error))
            logError(TAG,it)
        })
    }

    fun update(id:String){
        launch({
            val item = RentalItem()
            item.id = id
            item.userId = Proud.register.id
            item.title = Proud.register.name
            item.content = content.value.toString()
            item.label = "*$type"
            item.location = local
            item.image = ""
            if(done){
                item.done = 1
            }else{
                item.done = 0
            }
            item.startTime = DateUtil.nowDateTime
            item.endTime = time
            item.thumbUp = 0
            item.collect = 0
            item.comment = 0

            val response = repository.update(item)

            when(response.status){
                200 ->{
                    when {
                        imagesPath.isNullOrEmpty() -> {
                            showToast("发布成功")
                            val finishActivityEvent = FinishActivityEvent()
                            finishActivityEvent.category = Const.Like.RENTAL
                            EventBus.getDefault().post(finishActivityEvent)
                        }
                        imagesPath.size == 1 -> {
                            showToast("发布成功")
                            //upLoadImage(response.data,imagesPath[0])
                        }
                        imagesPath.size > 1 -> {
                            showToast("发布成功")
                            //upLoadImages(response.data,imagesPath)
                        }
                    }
                }
                500 ->{
                    showToast("发布失败")
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
                        showToast("发布成功")
                        val finishActivityEvent = FinishActivityEvent()
                        finishActivityEvent.category = Const.Like.RENTAL
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
                        showToast("发布成功")
                        val finishActivityEvent = FinishActivityEvent()
                        finishActivityEvent.category = Const.Like.RENTAL
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

        private const val TAG = "RentalActivityViewModel"

    }

}