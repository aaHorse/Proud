package com.horse.proud.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.horse.proud.data.SettingRepository

/**
 * @author liliyuan
 * @since 2020年6月5日07:18:33
 * */
class OverViewPublishedViewModelFactory(private val repository: SettingRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T: ViewModel?> create(modelClass: Class<T>):T{
        return OverViewPublishedViewModel(repository) as T
    }

}