package com.horse.proud.ui.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horse.core.proud.extension.logError
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.model.regist.Register
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.R
import com.horse.proud.data.SettingRepository
import kotlinx.coroutines.launch

/**
 * @author liliyuan
 * @since 2020年6月5日05:33:57
 * */
class OverViewPublishedViewModel(private val repository: SettingRepository):ViewModel() {

    private var flag = 1

    var taskCount = 0

    var lostCount = 0

    var rentalCount = 0

    var done = MutableLiveData<Int>()

    var name = "会飞的鱼"

    var info = GlobalUtil.getString(R.string.info)

    fun userMsg(id:Int){
        launch({
            val login = repository.userMsg(id)
            when(login.status){
                200 -> {
                    logWarn(TAG,login.data.toString())
                    //name = login.data.name
                    //info = login.data.info
                }
            }
        },{
            done.value = flag ++
            showToast(GlobalUtil.getString(R.string.unknown_error))
            logError(TAG,it)
        })
    }

    fun getCount(id:Int){
        userTaskCount(id)
        userLostCount(id)
        userRentalCount(id)
    }

    private fun userTaskCount(id:Int){
        launch({
            val list = repository.userTask(id)
            when(list.status){
                200 -> {
                    taskCount = list.taskList.size
                    done.value = flag ++
                }
            }
        },{
            done.value = flag ++
            showToast(GlobalUtil.getString(R.string.unknown_error))
            logError(TAG,it)
        })
    }

    private fun userLostCount(id:Int){
        launch({
            val list = repository.userLost(id)
            when(list.status){
                200 -> {
                    lostCount = list.lostList.size
                    done.value = flag ++
                }
            }
        },{
            done.value = flag ++
            showToast(GlobalUtil.getString(R.string.unknown_error))
            logError(TAG,it)
        })
    }

    private fun userRentalCount(id:Int){
        launch({
            val list = repository.userRental(id)
            when(list.status){
                200 -> {
                    rentalCount = list.rentalList.size
                    done.value = flag ++
                }
            }
        },{
            done.value = flag ++
            showToast(GlobalUtil.getString(R.string.unknown_error))
            logError(TAG,it)
        })
    }

    private fun launch(block:suspend () -> Unit,error: suspend (Throwable) -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (e:Throwable){
            error(e)
        }
    }

    companion object{

        private const val TAG = "OverViewPublishedViewModel"

    }

}