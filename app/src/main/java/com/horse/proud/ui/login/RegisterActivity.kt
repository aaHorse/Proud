package com.horse.proud.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.horse.proud.R
import com.horse.proud.data.model.regist.Register
import com.horse.proud.databinding.ActivityRegisterBinding
import com.horse.proud.event.RegisterSucceedEvent
import com.horse.proud.ui.common.BaseActivity
import com.horse.proud.ui.home.MainActivity
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.inject

/**
 * 注册界面
 *
 * @author liliyuan
 * @since 2020年5月2日14:21:59
 * */
class RegisterActivity : BaseActivity() {

    private val viewModelFactory by inject<RegisterActivityViewModelFactory>()

    val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(RegisterActivityViewModel::class.java) }
    private lateinit var register:Register

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityRegisterBinding>(this,R.layout.activity_register)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun setupViews() {
        btn_verify.setOnClickListener {
            register = Register()
            register.name = et_name.text.toString()
            register.number = et_number.text.toString()
            register.password = et_password.text.toString()
            viewModel.register(register)
        }
        btn_visit.setOnClickListener {
            MainActivity.actionStart(this)
        }
        observe()
    }

    private fun observe(){
        viewModel.dataChanged.observe(this, Observer {
            var event = RegisterSucceedEvent()
            event.register = register
            EventBus.getDefault().post(event)
            finish()
        })
    }

    companion object{

        private const val TAG = "RegisterActivity"

        fun actionStart(activity: Activity){
            val intent = Intent(activity,
                RegisterActivity::class.java)
            activity.startActivity(intent)
        }

    }

}
