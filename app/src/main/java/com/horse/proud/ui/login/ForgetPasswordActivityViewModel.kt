package com.horse.proud.ui.login


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horse.core.proud.extension.logError
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.R
import com.horse.proud.data.LoginRepository
import kotlinx.coroutines.launch

/**
 * @author liliyuan
 * @since 2020年6月1日19:24:41
 * */
class ForgetPasswordActivityViewModel(private val respository: LoginRepository): ViewModel() {

    var dataChanged = MutableLiveData<Int>()

    fun forgetPassword(number:String,phoneNumber:String,password:String) {
        launch ({
            if(number.isBlank()||password.isBlank()){
                showToast(GlobalUtil.getString(R.string.input_error))
            }else{
                val response = respository.forGetPassword(number,phoneNumber,password)
                when(response.status){
                    200->{
                        dataChanged.value = dataChanged.value?.plus(1)
                    }
                    else -> {
                        showToast(GlobalUtil.getString(R.string.change_password_error))
                    }
                }
            }
        }, {
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

    companion object {

        private const val TAG = "ForgetPasswordViewModel"

    }

}