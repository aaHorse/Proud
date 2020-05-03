package com.horse.proud.ui.login.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.horse.core.proud.Const
import com.horse.core.proud.util.SharedUtil
import com.horse.proud.R
import com.horse.proud.databinding.ActivityLoginBinding
import com.horse.proud.event.FinishActivityEvent
import com.horse.proud.event.MessageEvent
import com.horse.proud.network.model.Version
import com.horse.proud.ui.home.MainActivity
import com.horse.proud.ui.login.LoginActivityViewModel
import com.horse.proud.ui.login.LoginActivityViewModelFactory
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
class LoginActivity :AuthActivity(){

    val viewModelFactory by inject<LoginActivityViewModelFactory>()

    val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(LoginActivityViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this,R.layout.activity_login)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun setupViews() {
        super.setupViews()
        tv_register_account.setOnClickListener {
            RegisterActivity.actionStart(this)
        }
        btn_login.setOnClickListener {
            viewModel.login(number.text.toString(),password.text.toString())
        }
        observe()
    }

    override fun forwardToMainActivity() {
        // 登录成功，跳转到应用主界面
        MainActivity.actionStart(this)
        finish()
    }

    private fun observe(){
        viewModel.dataChanged.observe(this, Observer {
            SharedUtil.save(Const.Auth.USER_ID,"221701414")
            SharedUtil.save(Const.Auth.TOKEN,"token")
            SharedUtil.save(Const.Auth.LOGIN_TYPE,2)
            forwardToMainActivity()
        })
    }

    /**
     * 登录完成后，销毁这个活动。
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMessageEvent(messageEvent: MessageEvent) {
        if (messageEvent is FinishActivityEvent && LoginActivity::class.java == messageEvent.activityClass) {
            finish()
        }
    }

    companion object {

        private const val TAG = "LoginActivity"

        @JvmStatic
        val INTENT_HAS_NEW_VERSION = "intent_has_new_version"

        @JvmStatic
        val INTENT_VERSION = "intent_version"

        fun actionStart(activity: Activity,hasNewVersion:Boolean,version: Version?){
            val intent = Intent(activity,LoginActivity::class.java).apply {
                putExtra(INTENT_HAS_NEW_VERSION,hasNewVersion)
                putExtra(INTENT_VERSION,version)
            }
            activity.startActivity(intent)
        }

    }

}