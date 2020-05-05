package com.horse.proud.ui.rental

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logError
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.R
import com.horse.proud.data.RentalRepository
import com.horse.proud.data.model.rental.RentalItem
import com.horse.proud.util.DateUtil
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * @author liliyuan
 * @since 2020年5月5日13:43:27
 * */
class RentalActivityViewModel(private val repository: RentalRepository) : ViewModel() {

    var content = MutableLiveData<String>()

    var local:String = ""

    var type:String = ""

    var time:String = ""

    var imagePath:String? = null

    fun publish(){
        launch({
            var item = RentalItem()
            item.userId = Proud.getUserId()
            logWarn(TAG,"${item.userId}")
            item.title = "会飞的鱼"
            item.content = content.value.toString()
            item.label = type
            item.location = local
            item.image = ""
            item.done = 0
            item.startTime = DateUtil.nowDateTime
            item.endTime = time
            item.thumbUp = 0
            item.collect = 0
            item.comment = 0

            val response = repository.publish(item)

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

        private const val TAG = "RentalActivityViewModel"

    }

}