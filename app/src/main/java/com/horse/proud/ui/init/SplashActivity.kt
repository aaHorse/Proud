package com.horse.proud.ui.init

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.horse.core.proud.Const
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.R
import com.horse.proud.databinding.ActivitySplashBinding
import com.horse.proud.event.FinishActivityEvent
import com.horse.proud.event.MessageEvent
import com.horse.proud.network.model.Version
import com.horse.proud.ui.common.BaseActivity
import com.horse.proud.ui.home.MainActivity
import com.horse.proud.ui.login.LoginActivity
import constant.UiType
import constant.UiType.PLENTIFUL
import listener.Md5CheckResultListener
import listener.OnBtnClickListener
import listener.UpdateDownloadListener
import model.UiConfig
import model.UpdateConfig
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import update.UpdateAppUtils
import update.UpdateAppUtils.apkUrl
import update.UpdateAppUtils.getInstance
import update.UpdateAppUtils.updateContent
import update.UpdateAppUtils.updateTitle


/**
 * 进入APP的第一个界面，闪屏界面、进行内容初始化。
 *
 * @author liliyuan
 * @since 2020年4月8日06:01:46
 * */
class SplashActivity : BaseActivity(){

    private val viewModelFactory by inject<SplashActivityViewModelFactory>()

    val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(SplashActivityViewModel::class.java) }

    /**
     * 记录进入SplashActivity的时间。
     */
    private var enterTime: Long = 0

    /**
     * 判断是否正在跳转到下一个界面。
     */
    private var isForwarding = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UpdateAppUtils.init(this)
        val binding = DataBindingUtil.setContentView<ActivitySplashBinding>(this,R.layout.activity_splash)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        //delayToForward()
    }

    override fun setupViews() {
        enterTime = System.currentTimeMillis()
        Proud.refreshUserMsg()
        viewModel.checkNewVersion()
        observe()
}

    override fun onBackPressed() {
        // 屏蔽手机的返回键
    }

    private fun observe(){
        viewModel.newVersoinPath.observe(this, Observer {
            if(it.isNullOrEmpty()){
                forwardToNextActivity()
            }else{
                update(it)
            }
        })
        viewModel.noNewVersion.observe(this, Observer {
            viewModel.noNewVersion.value?.run {
                forwardToNextActivity()
            }
        })
    }

    private fun update(newVersionPath:String) {
        // ui配置
        val uiConfig = UiConfig().apply {
            uiType = PLENTIFUL
            cancelBtnText = "下次再说"
            titleTextColor = Color.BLACK
            titleTextSize = 18f
            contentTextColor = Color.parseColor("#88e16531")
        }

        // 更新配置
        val updateConfig = UpdateConfig().apply {
            //是否强制更新
            force = false
            checkWifi = true
            alwaysShow = true
            alwaysShowDownLoadDialog = true
            val file = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            if(file == null){
                //创建文件目录失败，不更新
                forwardToNextActivity()
            }else{
                apkSavePath = file.absolutePath + "/downloads"
            }
            apkSaveName = GlobalUtil.getString(R.string.apkSaveName)
        }

        getInstance()
            .apkUrl(newVersionPath)
            .updateTitle(GlobalUtil.getString(R.string.update_title))
            .updateContent(GlobalUtil.getString(R.string.update_content))
            .updateConfig(updateConfig)
            .uiConfig(uiConfig)
            .setCancelBtnClickListener(object : OnBtnClickListener{
                override fun onClick(): Boolean {
                    forwardToNextActivity()
                    return false
                }

            })
            .setUpdateDownloadListener(object : UpdateDownloadListener{
                override fun onDownload(progress: Int) {
                    //
                }

                override fun onError(e: Throwable) {
                    //
                }

                override fun onFinish() {
                    showToast("安装包下载完成")
                }

                override fun onStart() {
                    //
                }

            })
            .update()


    }

    /**
     * 成功登陆之后，这个活动将不再需要，通过EventBus通知销毁。
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMessageEvent(messageEvent: MessageEvent) {
        if (messageEvent is FinishActivityEvent) {
            finish()
        }
    }

    /**
     * 设置闪屏界面的最大延迟跳转，让用户不至于在闪屏界面等待太久。
     */
    private fun delayToForward() {
        Thread(Runnable {
            GlobalUtil.sleep(MAX_WAIT_TIME.toLong())
            forwardToNextActivity()
        }).start()
    }

    /**
     * 跳转到下一个Activity。
     * */
    @Synchronized
    fun forwardToNextActivity() {
        if(isForwarding){
            //如果正在跳转到下一个界面
            isForwarding = false
            val currentTime = System.currentTimeMillis()
            val timeSpent = currentTime - enterTime
            if(timeSpent < MIN_WAIT_TIME){
                GlobalUtil.sleep(MIN_WAIT_TIME - timeSpent)
            }
            runOnUiThread {
                if(Proud.loginState != Const.Auth.UNCHECK){
                    MainActivity.actionStart(this,userId = Proud.register.id)
                    finish()
                }else{
                    LoginActivity.actionStart(this)
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
        const val MIN_WAIT_TIME = 1000

        /**
         * 应用程序在闪屏界面最长的停留时间。
         */
        const val MAX_WAIT_TIME = 1000

        fun actionStart(activity:Activity){
            val intent = Intent(activity,SplashActivity::class.java)
            activity.startActivity(intent)
        }

    }


}