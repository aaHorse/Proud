package com.horse.proud.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.horse.proud.data.LoginRepository

/**
 * @author liliyuan
 * @since 2020年5月2日15:28:49
 * */
class LoginActivityViewModelFactory(private val repository: LoginRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginActivityViewModel(repository) as T
    }

}