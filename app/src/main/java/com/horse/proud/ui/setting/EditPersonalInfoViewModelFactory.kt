package com.horse.proud.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.horse.proud.data.EditPersonalRepository

/**
 * @author liliyuan
 * @since 2020年6月4日20:05:34
 * */
class EditPersonalInfoViewModelFactory(private val repository: EditPersonalRepository):ViewModelProvider.NewInstanceFactory() {

    override fun <T:ViewModel?> create(modelClass: Class<T>):T{
        return EditPersonalInfoViewModel(repository) as T
    }

}