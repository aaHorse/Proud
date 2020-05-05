package com.horse.proud.ui.lost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.horse.proud.data.LostRepository

/**
 * @author liliyuan
 * @since 2020年5月5日13:36:34
 * */
class LostActivityViewModelFactory(private val repository: LostRepository): ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LostActivityViewModel(repository) as T
    }

}