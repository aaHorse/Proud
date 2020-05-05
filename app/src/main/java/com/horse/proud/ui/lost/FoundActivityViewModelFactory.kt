package com.horse.proud.ui.lost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.horse.proud.data.LostRepository

/**
 * @author liliyuan
 * @since 2020年5月5日13:52:34
 * */
class FoundActivityViewModelFactory(private val repository: LostRepository): ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FoundActivityViewModel(repository) as T
    }

}