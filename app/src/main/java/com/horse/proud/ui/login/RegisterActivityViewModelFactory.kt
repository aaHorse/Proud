package com.horse.proud.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.horse.proud.data.LoginRepository

/**
 * @author liliyuan
 * @since 2020年5月4日08:59:46
 * */
class RegisterActivityViewModelFactory(private val repository: LoginRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegisterActivityViewModel(repository) as T
    }

}