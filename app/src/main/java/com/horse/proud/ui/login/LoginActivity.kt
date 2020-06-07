package com.horse.proud.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.horse.core.proud.Const
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.showToast
import com.horse.proud.R
import com.horse.proud.databinding.ActivityLoginBinding
import com.horse.proud.event.FinishActivityEvent
import com.horse.proud.event.MessageEvent
import com.horse.proud.event.RegisterEvent
import com.horse.proud.ui.common.BaseActivity
import com.horse.proud.ui.home.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject

/**
 * 登录界面。
 *
 * @author liliyuan
 * @since 2020年4月21日06:14:51
 * */
class LoginActivity : BaseActivity(){

    private val viewModelFactory by inject<LoginActivityViewModelFactory>()

    val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(LoginActivityViewModel::class.java) }

    private var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this,R.layout.activity_login)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun setupViews() {
        tv_register_account.setOnClickListener {
            RegisterActivity.actionStart(this)
        }
        btn_login.setOnClickListener {
            viewModel.login(number.text.toString(),password.text.toString())
        }
        tv_forget_password.setOnClickListener {
            ForgetPasswordActivity.actionStart(this)
        }
        observe()
    }

    private fun observe(){
        viewModel.dataChanged.observe(this, Observer {
            AuthUtil.saveAuthData(viewModel.login.data)
            AuthUtil.saveAuthState(Const.Auth.COMFIR)
            MainActivity.actionStart(this,userId = Proud.register.id)
            finish()
        })

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMessageEvent(messageEvent: MessageEvent) {
        when {
            messageEvent is FinishActivityEvent -> {
                finish()
            }
            messageEvent is RegisterEvent -> {
                with(messageEvent){
                    when(loginState){
                        Const.Auth.VISITOR -> {
                            AuthUtil.saveAuthState(Const.Auth.VISITOR)
                            MainActivity.actionStart(this@LoginActivity,userId = 0)
                        }
                        Const.Auth.COMFIR -> {
                            number.setText(register.number)
                            password.setText(register.password)
                            name = register.name
                        }
                    }
                }
            }
        }
    }

    companion object {

        private const val TAG = "LoginActivity"

        fun actionStart(activity: Activity){
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
        }

    }

}