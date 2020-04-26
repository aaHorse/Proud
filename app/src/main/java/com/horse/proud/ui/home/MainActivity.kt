package com.horse.proud.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.horse.proud.R
import com.horse.proud.ui.lost.LostFragment
import com.horse.proud.ui.rental.RentalFragment
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

