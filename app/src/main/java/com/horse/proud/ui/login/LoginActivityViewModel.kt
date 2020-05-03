package com.horse.proud.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horse.core.proud.extension.logError
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.R
import com.horse.proud.data.LoginRepository
import kotlinx.coroutines.launch

/**
 * @author liliyuan
 * @since 2020年5月2日15:26:23
 * */
class LoginActivityViewModel(private val respository:LoginRepository):ViewModel() {

    var dataChanged = MutableLiveData<Int>()

    fun login(number:String?,password:String?) {
        dataChanged.value = dataChanged.value?.plus(1)
        launch ({
            if(number.isNullOrBlank()||password.isNullOrBlank()){
                showToast(GlobalUtil.getString(R.string.input_error))
            }else{
                val login = respository.login(number,password)
                when(login.status){
                    200L->{
                        dataChanged.value = dataChanged.value?.plus(1)
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

        private const val TAG = "LoginActivityViewModel"

    }

}