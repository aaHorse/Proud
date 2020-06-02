package com.horse.proud.ui.init

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.horse.proud.data.SplashRepository

/**
 * @author liliyuan
 * @since 2020年6月2日16:06:59
 * */
class SplashActivityViewModelFactory(private val respository:SplashRepository): ViewModelProvider.NewInstanceFactory()  {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashActivityViewModel(respository) as T
    }

}