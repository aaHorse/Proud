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

    var flag:Int = 0

    var newVersoinPath = MutableLiveData<String>()

    var noNewVersion = MutableLiveData<Int>()

    fun checkNewVersion(){
        launch({
            var result = respository.checkNewVersion("${GlobalUtil.appVersionCode}")
            if(result.status == 100){
                //有新版本
                newVersoinPath.value = result.data
            }else{
                noNewVersion.value = flag ++
            }
        },{
            showToast(GlobalUtil.getString(R.string.unknown_error))
            logError(TAG,it)
            noNewVersion.value = flag ++
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