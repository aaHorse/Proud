package com.horse.proud.ui.rental

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logWarn
import com.horse.proud.data.RentalRepository
import com.horse.core.proud.model.Response
import com.horse.core.proud.model.other.CommentItem
import com.horse.core.proud.model.rental.RentalItem
import kotlinx.coroutines.launch

class RentalFragmentViewModel(private val repository: RentalRepository) : ViewModel(){

    var flag:Int = 0

    var isLoadingMore = MutableLiveData<Boolean>()

    var loadFailed = MutableLiveData<Int>()

    var isNoMoreData = MutableLiveData<Boolean>()

    var rentalItems = ArrayList<RentalItem>()

    var rentalItemsChanged = MutableLiveData<Int>()

    fun getRental(flag:Int,userID:Int){
        if(flag == 0){
            getAllRental()
        }else{
            getUserRental(userID)
        }
    }

    private fun getAllRental() {
        launch ({
            var rentalList = repository.getRentalList()
            when(rentalList.status){
                200 -> {
                    rentalItems.clear()
                    for(item in rentalList.rentalList){
                        rentalItems.add(item)
                    }
                    getComments()
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

    private fun getUserRental(userID: Int) {
        launch ({
            var rentalList = repository.userRental(userID)
            when(rentalList.status){
                200 -> {
                    rentalItems.clear()
                    for(item in rentalList.rentalList){
                        rentalItems.add(item)
                    }
                    getComments()
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

    fun publishComment(comment: CommentItem){
        launch({
            var response: Response = repository.publishComment(comment)
            when(response.status){
                200 -> {
                    //showToast("成功")
                }
                500 -> {
                    //showToast("失败1")
                }
            }
        },{
            logWarn(TAG,it)
            //showToast("失败2")
        })
    }

    private fun getComments(){
        for((index,item) in rentalItems.withIndex()){
            launch({
                logWarn(TAG,item.id)
                val commentList = repository.getComments(item.id)
                when(commentList.status){
                    200 -> {
                        item.comments = commentList
                        logWarn(TAG,"$index")
                        if(index == rentalItems.size-1){
                            isLoadingMore.value = false
                            rentalItemsChanged.value = flag++
                        }
                    }
                    500 -> {
                        logWarn(TAG,commentList.msg)
                    }
                }
            },{
                logWarn(TAG,it)
            })
        }
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