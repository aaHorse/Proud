package com.horse.proud.ui.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.horse.proud.R
import com.horse.proud.ui.common.BaseActivity

/**
 * 编辑个人信息
 *
 * @author liliyuan
 * @since 2020年5月28日10:37:31
 * */
class EditPersonalInfoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_personal_info)
    }

    companion object{

        fun actionStart(activity: Activity){
            val intent = Intent(activity,
                EditPersonalInfoActivity::class.java)
            activity.startActivity(intent)
        }

    }

}
