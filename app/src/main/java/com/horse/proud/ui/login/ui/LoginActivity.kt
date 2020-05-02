package com.horse.proud.ui.login.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.lifecycle.ViewModelProviders
import com.horse.proud.network.model.Version
import com.horse.proud.R
import com.horse.proud.callback.LoadDataListener
import com.horse.proud.event.FinishActivityEvent
import com.horse.proud.event.MessageEvent
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
class LoginActivity :AuthActivity(), LoadDataListener {

    private lateinit var timer: CountDownTimer

    /**
     * 是否正在登录中。
     * */
    private var isLogin = false

    val viewModelFactory by inject<LoginActivityViewModelFactory>()

    val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(LoginActivityViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel.login()
        btn_login.setOnClickListener {
            forwardToMainActivity()
        }
        tv_register_account.setOnClickListener {
            RegisterActivity.actionStart(this)
        }
    }

    override fun forwardToMainActivity() {
        // 登录成功，跳转到应用主界面
        MainActivity.actionStart(this)
        finish()
    }

    override fun setupViews() {
        //启用加载 UI
        super.setupViews()

    }

    override fun onLoad() {
        TODO("Not yet implemented")
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