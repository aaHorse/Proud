package com.horse.proud.ui.common

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewStub
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.horse.proud.R
import com.horse.proud.event.MessageEvent
import com.horse.proud.util.ActivityCollector
import com.horse.proud.callback.PermissionListener
import com.horse.proud.callback.RequestLifecycle
import com.horse.proud.event.ForceToLoginEvent
import com.horse.proud.ui.login.LoginActivity
import com.umeng.analytics.MobclickAgent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference

/**
 * @author liliyuan
 * @since 2020年4月7日15:51:56
 * */
open class BaseActivity : AppCompatActivity(), RequestLifecycle {

    protected var isActive: Boolean = false

    protected var activity: Activity? = null

    protected var loading: ProgressBar? = null

    private var loadErrorView: View? = null

    private var badNetworkView: View? = null

    private var noContentView: View? = null

    private var weakRefActivity: WeakReference<Activity>? = null

    var toolbar: Toolbar? = null

    private var progressDialog: ProgressDialog? = null

    private var mListener: PermissionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        weakRefActivity = WeakReference(this)
        ActivityCollector.add(weakRefActivity)
        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
        isActive = true

        /*
        * 友盟，实现时长统计。
        * */
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        isActive = false
        MobclickAgent.onPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity = null
        ActivityCollector.remove(weakRefActivity)
        EventBus.getDefault().unregister(this)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setupViews()
    }

    protected open fun setupViews() {
        /*
        * 在不需要显示加载的页面，这个方法会被覆盖，在需要的界面，这个方法会
        * super.setupViews()
        * */
        loading = findViewById(R.id.loading)
    }

    protected fun setupToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * 将状态栏设置成透明。
     * 适配Android 5.0以上系统。
     * */
    protected fun transparentStatusBar(){
        if (com.horse.core.proud.util.AndroidVersion.hasLollipop()) {
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    /**
     * 隐藏软键盘。
     * */
    fun hideSoftKeyboard() {
        try {
            val view = currentFocus
            if (view != null) {
                val binder = view.windowToken
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        } catch (e: Exception) {
            com.horse.core.proud.extension.logWarn(TAG, e.message, e)
        }
    }

    /**
     * 显示软键盘。
     */
    fun showSoftKeyboard(editText: EditText?) {
        try {
            if (editText != null) {
                editText.requestFocus()
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.showSoftInput(editText, 0)
            }
        } catch (e: Exception) {
            com.horse.core.proud.extension.logWarn(TAG, e.message, e)
        }
    }

    /**
     * 加载内容错误界面。
     *
     * @param tip 错误信息
     * */
    protected fun showLoadErrorView(tip: String) {
        if (loadErrorView != null) {
            loadErrorView?.visibility = View.VISIBLE
            return
        }
        val viewStub = findViewById<ViewStub>(R.id.loadErrorView)
        if (viewStub != null) {
            loadErrorView = viewStub.inflate()
            val loadErrorText = loadErrorView?.findViewById<TextView>(R.id.loadErrorText)
            loadErrorText?.text = tip
        }
    }

    /**
     * 加载时出现网络问题。
     *
     * @param listener 重新加载
     * */
    protected fun showBadNetworkView(listener: View.OnClickListener) {
        if (badNetworkView != null) {
            badNetworkView?.visibility = View.VISIBLE
            return
        }
        val viewStub = findViewById<ViewStub>(R.id.badNetworkView)
        if (viewStub != null) {
            badNetworkView = viewStub.inflate()
            val badNetworkRootView = badNetworkView?.findViewById<View>(R.id.badNetworkRootView)
            badNetworkRootView?.setOnClickListener(listener)
        }
    }

    /**
     * 空白界面。
     *
     * @param tip 空白界面的说明文字
     * */
    protected fun showNoContentView(tip: String) {
        if (noContentView != null) {
            noContentView?.visibility = View.VISIBLE
            return
        }
        val viewStub = findViewById<ViewStub>(R.id.noContentView)
        if (viewStub != null) {
            noContentView = viewStub.inflate()
            val noContentText = noContentView?.findViewById<TextView>(R.id.noContentText)
            noContentText?.text = tip
        }
    }

    protected fun hideLoadErrorView() {
        loadErrorView?.visibility = View.GONE
    }

    protected fun hideNoContentView() {
        noContentView?.visibility = View.GONE
    }

    protected fun hideBadNetworkView() {
        badNetworkView?.visibility = View.GONE
    }

    fun showProgressDialog(title: String?, message: String) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this).apply {
                if (title != null) {
                    setTitle(title)
                }
                setMessage(message)
                setCancelable(false)
            }
        }
        progressDialog?.show()
    }

    fun closeProgressDialog() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(messageEvent: MessageEvent) {
        if (messageEvent is ForceToLoginEvent) {
            if (isActive) { // 判断Activity是否在前台，防止非前台的Activity也处理这个事件，造成打开多个LoginActivity的问题。
                // force to login
                ActivityCollector.finishAll()
                LoginActivity.actionStart(this)
            }
        }
    }

    open fun permissionsGranted() {
        // 由子类来进行具体实现
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @CallSuper
    override fun startLoading() {
        loading?.visibility = View.VISIBLE
        hideBadNetworkView()
        hideNoContentView()
        hideLoadErrorView()
    }

    @CallSuper
    override fun loadFinished() {
        loading?.visibility = View.GONE
    }

    @CallSuper
    override fun loadFailed(msg: String?) {
        loading?.visibility = View.GONE
    }

    companion object{
        private const val TAG = "BaseActivity"
    }
}