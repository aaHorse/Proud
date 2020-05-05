package com.horse.proud.ui.rental

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logWarn
import com.horse.proud.data.RentalRepository
import com.horse.proud.data.model.rental.RentalItem
import kotlinx.coroutines.launch

class RentalFragmentViewModel(private val repository: RentalRepository) : ViewModel(){

    var flag:Int = 0

    var isLoadingMore = MutableLiveData<Boolean>()

    var loadFailed = MutableLiveData<Int>()

    var isNoMoreData = MutableLiveData<Boolean>()

    var rentalItems = ArrayList<RentalItem>()

    var rentalItemsChanged = MutableLiveData<Int>()

    fun getRentalList() {
        launch ({
            var rentalList = repository.getRentalList()
            when(rentalList.status){
                200 -> {
                    rentalItems.clear()
                    for(item in rentalList.rentalList){
                        rentalItems.add(item)
                    }
                    isLoadingMore.value = false
                    rentalItemsChanged.value = flag++
                }
                500 -> {
                    loadFailed.value = flag++
                }
            }

        }, {
            logWarn(TAG, it.message, it)
            loadFailed.value = flag++
        })
    }

    fun like(id:String){
        launch({
            repository.like(id)
        },{
            logWarn(TAG,it)
        })
    }

    private fun launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            error(e)
        }
    }

    companion object {

        private const val TAG = "RentalFragmentViewModel"

    }

}