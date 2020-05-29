package com.horse.proud.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.horse.core.proud.Const
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.R
import com.horse.proud.event.MessageEvent
import com.horse.proud.event.ScrollEvent
import com.horse.proud.ui.about.AboutActivity
import com.horse.proud.ui.common.BaseActivity
import com.horse.proud.ui.lost.FoundActivity
import com.horse.proud.ui.lost.LostActivity
import com.horse.proud.ui.lost.LostFragment
import com.horse.proud.ui.rental.RentalActivity
import com.horse.proud.ui.rental.RentalFragment
import com.horse.proud.ui.setting.EditPersonalInfoActivity
import com.horse.proud.ui.setting.OverViewPublishedActivity
import com.horse.proud.ui.task.TaskActivity
import com.horse.proud.ui.task.TaskFragment
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialog.MessageDialogBuilder
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_navigation.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 主界面
 *
 * @author liliyuan
 * @since 2020年4月24日19:55:22
 * */
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    /**
     * 标志位
     * 查看全部发布：0
     * 查看个人发布：1
     * 查看失物招领：2
     * 查看物品租赁：3
     * */
    var flag:Int = 0

    /**
     * 任务发布列表 Fragment
     * */
    var taskFragment: TaskFragment? = null

    /**
     * 失物招领列表
     * */
    var lostFragment: LostFragment? = null

    /**
     * 物品租赁列表
     * */
    var rentalFragment: RentalFragment? = null

    /**
     * 当前默认选中
     * */
    private var currentId:Int = R.id.tv_task

    /**
     * 底部导航栏是否正在显示
     * */
    private var isBottomShow:Boolean = true

    private var backPressTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN or
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
        flag = intent.getIntExtra(Const.ACTIVITY_FLAG,0)
        setContentView(R.layout.activity_main)
        //上面这个方法下面的代码不会被执行
    }

    override fun setupViews() {
        changeFragment(getFragmentResId())

        tv_task.setOnClickListener(tabClickListener)
        tv_lost_and_found.setOnClickListener(tabClickListener)
        tv_rental.setOnClickListener(tabClickListener)

        navView.setNavigationItemSelectedListener(this)
        navView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                navView.viewTreeObserver.removeOnPreDrawListener(this)
                return false
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMessageEvent(messageEvent: MessageEvent) {
        when(messageEvent){
            is ScrollEvent -> {
                with(messageEvent){
                    if (scrollY - oldScrollY > 0 && isBottomShow) {
                        //上滑
                        isBottomShow = false
                        bottom.animate().translationY(bottom.height.toFloat())
                    } else if(scrollY - oldScrollY < 0 && !isBottomShow) {
                        //下滑
                        isBottomShow = true
                        bottom.animate().translationY(0.0f)
                    }
                    null
                }
            }
        }
    }

    private val tabClickListener = View.OnClickListener { v ->
            changeSelect(v.id)
            if (v.id != currentId) {
                changeFragment(v.id)
                currentId = v.id
            }
        }

    private fun getFragmentResId():Int{
        return when(flag){
            1 -> R.id.tv_task
            2 -> R.id.tv_lost_and_found
            3 -> R.id.tv_rental
            else -> R.id.tv_task
        }
    }

    private fun changeFragment(resId: Int){
        changeSelect(resId)
        var transaction:FragmentTransaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        when (resId) {
            R.id.tv_task -> {
                if(taskFragment == null){
                    taskFragment = TaskFragment()
                    transaction.add(R.id.main_container,taskFragment!!)
                }else{
                    transaction.show(taskFragment!!)
                }
            }
            R.id.tv_lost_and_found -> {
                if(lostFragment == null){
                    lostFragment = LostFragment()
                    transaction.add(R.id.main_container,lostFragment!!)
                }else{
                    transaction.show(lostFragment!!)
                }
            }
            R.id.tv_rental -> {
                if(rentalFragment == null){
                    rentalFragment = RentalFragment()
                    transaction.add(R.id.main_container,rentalFragment!!)
                }else{
                    transaction.show(rentalFragment!!)
                }
            }
        }
        transaction.commit()
        if(flag != 0 && ll_toolbar.visibility != View.VISIBLE){
            setupToolbar()
            ll_toolbar.visibility = View.VISIBLE
            main_container.animate().translationY(ll_toolbar.height.toFloat())
        }
    }

    private fun hideFragments(transaction: FragmentTransaction){
        taskFragment?.let {
            transaction.hide(it)
        }
        lostFragment?.let {
            transaction.hide(it)
        }
        rentalFragment?.let {
            transaction.hide(it)
        }
    }

    private fun changeSelect(resId:Int){
        tv_task.isChecked = false
        tv_lost_and_found.isChecked = false
        tv_rental.isChecked = false
        when (resId) {
            R.id.tv_task -> {
                tv_task.isChecked = true
            }
            R.id.tv_lost_and_found -> {
                tv_lost_and_found.isChecked = true
            }
            R.id.tv_rental -> {
                tv_rental.isChecked = true
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.task -> {
                TaskActivity.actionStart(this)
            }
            R.id.lost -> {
                FoundActivity.actionStart(this)
            }
            R.id.found -> {
                LostActivity.actionStart(this)
            }
            R.id.rental -> {
                RentalActivity.actionStart(this)
            }
            R.id.setting -> {
                getSettingType()
            }
            R.id.about -> {
                AboutActivity.actionStart(this)
            }
        }
        return true
    }

    private fun getSettingType() {
        val items = arrayOf(
            GlobalUtil.getString(R.string.setting_0),
            GlobalUtil.getString(R.string.setting_1),
            GlobalUtil.getString(R.string.setting_2))
        QMUIDialog.MenuDialogBuilder(this)
            .addItems(items) { dialog, which ->
                dialog.dismiss()
                when(which){
                    0 -> {
                        EditPersonalInfoActivity.actionStart(this)
                    }
                    1 -> {
                        OverViewPublishedActivity.actionStart(this)
                    }
                    2 -> {
                        exit()
                    }
                }
            }.create(R.style.MenuDialog).show()
    }

    /**
     * 退出登录
     * */
    private fun exit() {
        MessageDialogBuilder(this)
            .setTitle("退出登录")
            .setMessage("确定要退出吗？")
            .addAction("取消") { dialog, index ->
                dialog.dismiss()
            }
            .addAction(0, "退出", QMUIDialogAction.ACTION_PROP_NEGATIVE) { dialog, index ->
                dialog.dismiss()
            }.create(R.style.MenuDialog).show()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val now = System.currentTimeMillis()
            if (now - backPressTime > 2000) {
                showToast(String.format(GlobalUtil.getString(R.string.press_again_to_exit), GlobalUtil.appName))
                backPressTime = now
            } else {
                super.onBackPressed()
            }
        }
    }

    companion object{

        private const val TAG = "MainActivity"

        fun actionStart(activity: Activity,flag:Int = 0){
            val intent = Intent(activity,MainActivity::class.java)
            intent.putExtra(Const.ACTIVITY_FLAG,flag)
            activity.startActivity(intent)
        }

    }

}

