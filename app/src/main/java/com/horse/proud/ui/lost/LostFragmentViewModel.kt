package com.horse.proud.ui.lost

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logWarn
import com.horse.proud.data.LostRepository
import com.horse.proud.data.model.lost.LostItem
import kotlinx.coroutines.launch

/**
 * @author liliyuan
 * @since 2020年4月26日15:27:48
 * */
class LostFragmentViewModel(private val repository: LostRepository) : ViewModel(){

    var flag:Int = 0

    var isLoadingMore = MutableLiveData<Boolean>()

    var loadFailed = MutableLiveData<Int>()

    var isNoMoreData = MutableLiveData<Boolean>()

    var lostItems = ArrayList<LostItem>()

    var lostItemsChanged = MutableLiveData<Int>()

    fun getLost() {
        launch ({
            var lostList = repository.getLostList()
            when(lostList.status){
                200 -> {
                    lostItems.clear()
                    for(item in lostList.lostList){
                        lostItems.add(item)
                    }
                    isLoadingMore.value = false
                    lostItemsChanged.value = flag++
                }
                500 -> {
                    loadFailed.value = flag++
                }
            }

        }, {
            logWarn(TAG, it.message, it)
            loadFailed.value = flag++
            Toast.makeText(Proud.getContext(), it.message, Toast.LENGTH_SHORT).show()
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

        private const val TAG = "LostFragmentViewModel"

    }

}