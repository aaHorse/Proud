package com.horse.proud.ui.init.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.horse.core.proud.Const
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.util.GlobalUtil
import com.horse.core.proud.util.SharedUtil
import com.horse.proud.network.model.Init
import com.horse.proud.network.model.base.OriginThreadCallback
import com.horse.proud.network.model.base.Response
import com.horse.proud.network.model.Version
import com.horse.proud.R
import com.horse.proud.ui.common.BaseActivity
import com.horse.proud.event.FinishActivityEvent
import com.horse.proud.event.MessageEvent
import com.horse.proud.ui.home.MainActivity
import com.horse.proud.ui.login.ui.LoginActivity
import com.horse.proud.util.ResponseHandler
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 进入APP的第一个界面，闪屏界面、进行内容初始化。
 *
 * 没有做版本更新。
 *
 * @author liliyuan
 * @since 2020年4月8日06:01:46
 * */
class SplashActivity : BaseActivity(){

    /**
     * 记录进入SplashActivity的时间。
     */
    var enterTime: Long = 0

    /**
     * 判断是否正在跳转到下一个界面。
     */
    var isForwarding = false

    var hasNewVersion = false

    lateinit var logoView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        enterTime = System.currentTimeMillis()
        delayToForward()
    }

    override fun setupViews() {
        startInitRequest()
    }

    override fun onBackPressed() {
        // 屏蔽手机的返回键
    }

    /**
     * 成功登陆之后，这个活动将不再需要，通过EventBus通知销毁。
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMessageEvent(messageEvent: MessageEvent) {
        if (messageEvent is FinishActivityEvent && LoginActivity::class.java == messageEvent.activityClass) {
            finish()
        }
    }

    /**
     * 向服务器发送初始化请求。
     * */
    private fun startInitRequest(){
        Init.getResponse(object :
            OriginThreadCallback {
            override fun onResponse(response: Response) {
                if(activity == null){
                    return
                }
                var version: Version? = null
                val init = response as Init
                //初始化后，重定向base url
                Proud.BASE_URL = init.base
                //如果ResponseHandler不能处理
                if(!ResponseHandler.handleResponse(init)){
                    val status = init.status
                    if(status == 0){
                        val token = init.token
                        val avatar = init.avatar
                        val bgImage = init.bgImage
                        hasNewVersion = init.hasNewVersion
                        if(hasNewVersion){
                            version = init.version
                        }
                        //将登录信息写进SharedPreferences
                        if(!TextUtils.isEmpty(token)){
                            SharedUtil.save(Const.Auth.TOKEN,token)
                            if (!TextUtils.isEmpty(avatar)) {
                                SharedUtil.save(Const.User.AVATAR, avatar)
                            }
                            if (!TextUtils.isEmpty(bgImage)) {
                                SharedUtil.save(Const.User.BG_IMAGE, bgImage)
                            }
                            //刷新当前的登录信息
                            Proud.refreshLoginState()
                        }
                    }else{
                        logWarn(TAG,GlobalUtil.getResponseClue(status, init.msg))
                    }
                }
                forwardToNextActivity(hasNewVersion, version)
            }

            override fun onFailure(e: Exception) {
                logWarn(TAG, e.message, e)
                forwardToNextActivity(false, null)
            }
        })
    }

    /**
     * 设置闪屏界面的最大延迟跳转，让用户不至于在闪屏界面等待太久。
     */
    private fun delayToForward() {
        Thread(Runnable {
            GlobalUtil.sleep(MAX_WAIT_TIME.toLong())
            forwardToNextActivity(false, null)
        }).start()
    }

    /**
     * 跳转到下一个Activity。
     * */
    @Synchronized
    fun forwardToNextActivity(hasNewVersion: Boolean, version: Version?) {
        if(!isForwarding){
            //如果正在跳转到下一个界面
            isForwarding = true
            val currentTime = System.currentTimeMillis()
            val timeSpent = currentTime - enterTime
            if(timeSpent < MIN_WAIT_TIME){
                GlobalUtil.sleep(MIN_WAIT_TIME - timeSpent)
            }
            runOnUiThread {
                //测试
                if(!Proud.isLogin()){
                    MainActivity.actionStart(this)
                    finish()
                }else{
                    LoginActivity.actionStart(this,hasNewVersion,version)
                    finish()
                }
            }
        }
    }

    companion object {

        private const val TAG = "SplashActivity"

        /**
         * 应用程序在闪屏界面最短的停留时间。
         */
        const val MIN_WAIT_TIME = 2000

        /**
         * 应用程序在闪屏界面最长的停留时间。
         */
        const val MAX_WAIT_TIME = 5000
    }


}