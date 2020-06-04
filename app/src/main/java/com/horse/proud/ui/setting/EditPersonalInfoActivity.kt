package com.horse.proud.ui.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.horse.proud.R
import com.horse.proud.databinding.ActivityEditPersonalInfoBinding
import com.horse.proud.ui.common.BaseActivity
import kotlinx.android.synthetic.main.activity_edit_personal_info.*
import org.koin.android.ext.android.inject

/**
 * 编辑个人信息
 *
 * @author liliyuan
 * @since 2020年5月28日10:37:31
 * */
class EditPersonalInfoActivity : BaseActivity() {

    private val viewModelFactory by inject<EditPersonalInfoViewModelFactory>()

    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(
        EditPersonalInfoViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityEditPersonalInfoBinding>(this,R.layout.activity_edit_personal_info)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun setupViews() {
        setupToolbar()
        sure.setOnClickListener {
            viewModel.update()
        }
    }

    companion object{

        private const val TAG = "EditPersonalInfoActivity"

        fun actionStart(activity: Activity){
            val intent = Intent(activity,
                EditPersonalInfoActivity::class.java)
            activity.startActivity(intent)
        }

    }

}
