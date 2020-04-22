package com.horse.proud.ui.home

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.horse.proud.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object{

        private const val TAG = "MainActivity"

        fun actionStart(activity: Activity){
            val intent = Intent(activity,MainActivity::class.java)
            activity.startActivity(intent)
        }

    }

}
