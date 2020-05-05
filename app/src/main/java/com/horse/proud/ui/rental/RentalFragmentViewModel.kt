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

    /**
     * 监听 rentalItems
     * */
    var dataChanged = MutableLiveData<Int>()

    var isLoadingMore = MutableLiveData<Boolean>()

    var loadFailed = MutableLiveData<Int>()

    var isNoMoreData = MutableLiveData<Boolean>()

    var rentalItems = ArrayList<RentalItem>()

    fun getRentalList() {
        launch ({
            var rentalList = repository.getRentalList()
            for(item in rentalList.rentalList){
                rentalItems.add(item)
            }
            isLoadingMore.value = false
            dataChanged.value = dataChanged.value?.plus(1)
        }, {
            logWarn(TAG, it.message, it)
            loadFailed.value = dataChanged.value?.plus(1)
            Toast.makeText(Proud.getContext(), it.message, Toast.LENGTH_SHORT).show()
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