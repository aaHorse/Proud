package com.horse.proud.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horse.core.proud.extension.logError
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.R
import com.horse.proud.data.LoginRepository
import com.horse.proud.data.model.regist.Register
import kotlinx.coroutines.launch
import kotlin.isInitialized as isInitialized

/**
 * @author liliyuan
 * @since 2020年5月4日09:01:21
 * */
class RegisterActivityViewModel(private val respository: LoginRepository): ViewModel() {

    var dataChanged = MutableLiveData<Int>()

    var accessToken = MutableLiveData<String>()

    fun register(register: Register) {
        launch ({
            val response = respository.register(register)
            when(response.status){
                200 ->{
                    dataChanged.value = dataChanged.value?.plus(1)
                }
                500 ->{
                    showToast("用户名已存在")
                }
            }
        }, {
            showToast(GlobalUtil.getString(R.string.unknown_error))
            logError(TAG,it)
        })
    }

    fun getAccessToken(type:String,ak:String,sk:String){
        launch({
            val response = respository.getAccessToken(type, ak, sk)
            if(response.error.isEmpty()){
                accessToken.value = response.token
            }else{
                showToast(GlobalUtil.getString(R.string.get_token_failed))
                logError(TAG,response.des)
            }
        },{
            showToast(GlobalUtil.getString(R.string.get_token_failed))
            logError(TAG,it)
        })
    }

    fun photoToWords(token:String,image:String){
        launch({
            val response = respository.photoToWords(token, image)
            for(item in response.resultList){
                logWarn(TAG,item.words)
            }
        },{
            showToast(GlobalUtil.getString(R.string.get_token_failed))
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

    companion object {

        private const val TAG = "RegisterActivityViewModel"

    }

}