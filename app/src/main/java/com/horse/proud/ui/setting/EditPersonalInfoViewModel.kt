package com.horse.proud.ui.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logError
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.R
import com.horse.proud.data.EditPersonalRepository
import com.horse.proud.data.model.regist.Register
import kotlinx.coroutines.launch

/**
 * @author liliyuan
 * @since 2020年6月4日19:47:28
 * */
class EditPersonalInfoViewModel(private val repository: EditPersonalRepository):ViewModel() {

    var name = Proud.name

    var phone = Proud.phone

    var info = Proud.info

    fun update(){
        launch({
            val register = Register()
            register.name = name
            register.phoneNumber = phone
            register.info = info
            val response = repository.update(register)
            when(response.status){
                200 -> {
                    showToast("修改成功")
                }
                else -> {
                    showToast("修改失败")
                }
            }
        },{
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

        private const val TAG = "EditPersonalInfoViewModel"

    }

}