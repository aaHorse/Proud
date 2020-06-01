package com.horse.proud.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.horse.proud.data.LoginRepository

/**
 * @author liliyuan
 * @since 2020年6月1日19:25:03
 * */
class ForgetPasswordActivityViewModelFactory(private val repository: LoginRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ForgetPasswordActivityViewModel(repository) as T
    }

}