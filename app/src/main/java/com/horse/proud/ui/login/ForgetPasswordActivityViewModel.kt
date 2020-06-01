package com.horse.proud.ui.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horse.proud.data.LoginRepository
import kotlinx.coroutines.launch

/**
 * @author liliyuan
 * @since 2020年6月1日19:24:41
 * */
class ForgetPasswordActivityViewModel(private val respository: LoginRepository): ViewModel() {

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