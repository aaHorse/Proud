package com.horse.proud.ui.rental

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.horse.proud.data.RentalRepository

/**
 * @author liliyuan
 * @since 2020年4月25日09:28:25
 * */
class RentalFragmentViewModelFactory(private val repository: RentalRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RentalFragmentViewModel(repository) as T
    }

}