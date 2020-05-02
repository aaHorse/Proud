package com.horse.proud.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horse.core.proud.extension.logError
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToast
import com.horse.proud.data.LoginRepository
import kotlinx.coroutines.launch

/**
 * @author liliyuan
 * @since 2020年5月2日15:26:23
 * */
class LoginActivityViewModel(private val respository:LoginRepository):ViewModel() {

    fun login() {
        launch ({
            var login = respository.login("123456","123456","123456")
            showToast(login.errorMsg)

        }, {
            showToast("出错")
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