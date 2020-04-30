package com.horse.proud.ui.lost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.horse.proud.data.LostRepository

/**
 * @author liliyuan
 * @since 2020年4月26日15:26:47
 * */
class LostFragmentViewModelFactory(private val repository: LostRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LostFragmentViewModel(repository) as T
    }

}