package com.horse.proud.ui.lost

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logWarn
import com.horse.proud.data.LostRepository
import com.horse.proud.data.model.Response
import com.horse.proud.data.model.lost.LostItem
import com.horse.proud.data.model.other.CommentItem
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
                    getComments()
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
        for((index,item) in lostItems.withIndex()){
            launch({
                logWarn(TAG,item.id)
                val commentList = repository.getComments(item.id)
                when(commentList.status){
                    200 -> {
                        item.comments = commentList
                        logWarn(TAG,"$index")
                        if(index == lostItems.size-1){
                            isLoadingMore.value = false
                            lostItemsChanged.value = flag++
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

        private const val TAG = "LostFragmentViewModel"

    }

}