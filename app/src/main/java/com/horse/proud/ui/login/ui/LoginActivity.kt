package com.horse.proud.ui.login.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import com.horse.proud.network.model.Version
import com.horse.proud.R
import com.horse.proud.event.FinishActivityEvent
import com.horse.proud.event.MessageEvent
import com.horse.proud.ui.home.MainActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 登录界面。
 *
 * @author liliyuan
 * @since 2020年4月21日06:14:51
 * */
class LoginActivity :AuthActivity(){

    private lateinit var timer: CountDownTimer

    /**
     * 是否正在登录中。
     * */
    private var isLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        forwardToMainActivity()
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