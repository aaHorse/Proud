package com.horse.proud.ui.init

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horse.core.proud.extension.logError
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.R
import com.horse.proud.data.SplashRepository
import kotlinx.coroutines.launch

/**
 * @author liliyuan
 * @since 2020年6月2日15:44:51
 * */
class SplashActivityViewModel(private val respository:SplashRepository):ViewModel() {

    var newVersoinPath = MutableLiveData<String>()

    fun checkNewVersion(){
        launch({
            var result = respository.checkNewVersion()
            if(result.status == 100){
                //有新版本
                if(GlobalUtil.appVersionName != result.msg){
                    newVersoinPath.value = result.data
                }
            }
        },{
            showToast(GlobalUtil.getString(R.string.unknown_error))
            logError(TAG,it)
        })
    }

    private fun launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            error(e)
        }
    }

    companion object{

        private const val TAG = "SplashActivityViewModel"

    }

}