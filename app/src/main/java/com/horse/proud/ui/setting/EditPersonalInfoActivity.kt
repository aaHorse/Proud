package com.horse.proud.ui.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.horse.core.proud.Const
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.R
import com.horse.proud.databinding.ActivityEditPersonalInfoBinding
import com.horse.proud.event.EditInfoEvent
import com.horse.proud.ui.common.BaseActivity
import com.horse.proud.ui.login.AuthUtil
import kotlinx.android.synthetic.main.activity_edit_personal_info.*
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.inject

/**
 * 编辑个人信息
 *
 * @author liliyuan
 * @since 2020年5月28日10:37:31
 * */
class EditPersonalInfoActivity : BaseActivity() {

    private val viewModelFactory by inject<EditPersonalInfoViewModelFactory>()

    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(
        EditPersonalInfoViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityEditPersonalInfoBinding>(this,R.layout.activity_edit_personal_info)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun setupViews() {
        setupToolbar()
        observe()
        sure.setOnClickListener {
            if(Proud.loginState != Const.Auth.COMFIR){
                showToast(GlobalUtil.getString(R.string.visitor_reminder))
            }else{
                viewModel.update()
            }
        }
        edit_phone.setOnClickListener {
            showToast("经费不足，验证码功能只有UI >o<")
            comfir.visibility = View.VISIBLE
        }
    }

    private fun observe(){
        viewModel.dataUpdate.observe(this, Observer {
            showToast("修改成功")
            AuthUtil.saveAuthData(viewModel.register)
            EventBus.getDefault().post(EditInfoEvent())
            finish()
        })
    }

    companion object{

        private const val TAG = "EditPersonalInfoActivity"

        fun actionStart(activity: Activity){
            val intent = Intent(activity,
                EditPersonalInfoActivity::class.java)
            activity.startActivity(intent)
        }

    }

}
