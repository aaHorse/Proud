package com.horse.proud.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.horse.proud.R
import com.horse.proud.ui.common.BaseActivity

/**
 * 关于界面
 *
 * @author liliyuan
 * @since 2020年5月3日21:05:11
 * */
class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }

    override fun setupViews() {
        setupToolbar()
    }

    companion object{

        private const val TAG = "AboutActivity"

        fun actionStart(activity: Activity){
            val intent = Intent(activity,AboutActivity::class.java)
            activity.startActivity(intent)
        }

    }

}
