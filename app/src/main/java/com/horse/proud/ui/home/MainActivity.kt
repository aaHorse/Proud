package com.horse.proud.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.horse.proud.R
import com.horse.proud.ui.task.TaskFragment
import kotlinx.android.synthetic.main.bottom_navigation.*


/**
 * 主界面
 *
 * @author liliyuan
 * @since 2020年4月24日19:55:22
 * */
class MainActivity : AppCompatActivity() {
    /**
     * 任务发布列表 Fragment
     * */
    private lateinit var taskFragment: TaskFragment

    /**
     * 失物招领列表
     * */
    private lateinit var taskFragment2: TaskFragment

    /**
     * 物品租赁列表
     * */
    private lateinit var taskFragment3: TaskFragment

    /**
     * 当前默认选中
     * */
    private var currentId:Int = R.id.tv_task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskFragment = TaskFragment()
        supportFragmentManager.beginTransaction().add(R.id.main_container, taskFragment)
            .commit()

        tv_task.setOnClickListener(tabClickListener)
        tv_lost_and_found.setOnClickListener(tabClickListener)
        tv_rental.setOnClickListener(tabClickListener)

    }

    private val tabClickListener =
        View.OnClickListener { v ->
            if (v.id != currentId) {
                changeSelect(v.id)
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
                    transaction.add(R.id.main_container,taskFragment)
                }else{
                    transaction.show(taskFragment)
                }
            }
            R.id.tv_lost_and_found -> {
                if(taskFragment2 == null){
                    taskFragment3 = TaskFragment()
                    transaction.add(R.id.main_container,taskFragment2)
                }else{
                    transaction.show(taskFragment2)
                }
            }
            R.id.tv_rental -> {
                if(taskFragment3 == null){
                    taskFragment3 = TaskFragment()
                    transaction.add(R.id.main_container,taskFragment3)
                }else{
                    transaction.show(taskFragment3)
                }
            }
        }
        transaction.commit()
    }

    private fun hideFragments(transaction: FragmentTransaction){
        taskFragment?.let {
            transaction.hide(it)
        }
        taskFragment2?.let {
            transaction.hide(it)
        }
        taskFragment3?.let {
            transaction.hide(it)
        }
    }

    private fun changeSelect(resId:Int){
        tv_task.isSelected = false
        tv_lost_and_found.isSelected = false
        tv_rental.isSelected = false
        when (resId) {
            R.id.tv_task -> {
                tv_task.isSelected = true
            }
            R.id.tv_lost_and_found -> {
                tv_lost_and_found.isSelected = true
            }
            R.id.tv_rental -> {
                tv_rental.isSelected = true
            }
        }
    }

    companion object{

        private const val TAG = "MainActivity"

        fun actionStart(activity: Activity){
            val intent = Intent(activity,MainActivity::class.java)
            activity.startActivity(intent)
        }

    }

}

