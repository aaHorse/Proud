package com.horse.proud.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horse.core.proud.extension.logError
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.util.GlobalUtil
import com.horse.core.proud.util.SharedUtil
import com.horse.proud.R
import com.horse.proud.data.LoginRepository
import com.horse.core.proud.model.login.Login
import kotlinx.coroutines.launch

/**
 * @author liliyuan
 * @since 2020年5月2日15:26:23
 * */
class LoginActivityViewModel(private val respository:LoginRepository):ViewModel() {

    var dataChanged = MutableLiveData<Int>()

    lateinit var login: Login

    fun login(number:String,password:String) {
        launch ({
            if(number.isBlank()||password.isBlank()){
                showToast(GlobalUtil.getString(R.string.input_error))
            }else{
                login = respository.login(number,password)
                when(login.status){
                    200->{
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