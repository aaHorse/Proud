package com.horse.proud.ui.login.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.horse.proud.R

/**
 * 注册界面
 *
 * @author liliyuan
 * @since 2020年5月2日14:21:59
 * */
class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    companion object{

        private const val TAG = "RegisterActivity"

        fun actionStart(activity: Activity){
            val intent = Intent(activity,RegisterActivity::class.java)
            activity.startActivity(intent)
        }

    }

}
