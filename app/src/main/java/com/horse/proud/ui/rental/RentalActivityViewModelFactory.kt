package com.horse.proud.ui.rental

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.horse.proud.data.RentalRepository

/**
 * @author liliyuan
 * @since 2020年5月5日13:39:37
 * */
class RentalActivityViewModelFactory(private val repository: RentalRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RentalActivityViewModel(repository) as T
    }

}