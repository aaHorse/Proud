package com.horse.proud.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.horse.core.proud.extension.showToast
import com.horse.proud.R
import com.horse.proud.ui.lost.FoundActivity
import com.horse.proud.ui.lost.LostActivity
import com.horse.proud.ui.lost.LostFragment
import com.horse.proud.ui.rental.RentalActivity
import com.horse.proud.ui.rental.RentalFragment
import com.horse.proud.ui.task.TaskActivity
import com.horse.proud.ui.task.TaskFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_navigation.*

/**
 * 主界面
 *
 * @author liliyuan
 * @since 2020年4月24日19:55:22
 * */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskFragment = TaskFragment()
        supportFragmentManager.beginTransaction().add(R.id.main_container, taskFragment!!)
            .commit()

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

    private val tabClickListener =
        View.OnClickListener { v ->
            changeSelect(v.id)
            if (v.id != currentId) {
                changeFragment(v.id)
                currentId = v.id
            }
        }

    private fun changeFragment(resId: Int){
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
        var position = -1
        when (item.itemId) {
            R.id.task -> {
                position = 0
                TaskActivity.actionStart(this)
            }
            R.id.lost -> {
                position = 1
                FoundActivity.actionStart(this)
            }
            R.id.found -> {
                position = 2
                LostActivity.actionStart(this)
            }
            R.id.rental -> {
                position = 3
                RentalActivity.actionStart(this)
            }
        }
        showToast("点击位置：$position")
        return true
    }

    companion object{

        private const val TAG = "MainActivity"

        fun actionStart(activity: Activity){
            val intent = Intent(activity,MainActivity::class.java)
            activity.startActivity(intent)
        }

    }

}

