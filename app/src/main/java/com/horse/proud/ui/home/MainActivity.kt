package com.horse.proud.ui.home

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.horse.proud.R
import com.horse.proud.databinding.ActivityMainBinding
import com.horse.proud.repository.TestRepository
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    //val viewModelFactory by inject<TestModelFactory>()
    var viewModelFactory = TestModelFactory(TestRepository())

    val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(TestViewModel::class.java) }

    private val binding by lazy { DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        Runnable {
            viewModel.getGson()
        }.run()
    }

    companion object{

        private const val TAG = "MainActivity"

        fun actionStart(activity: Activity){
            val intent = Intent(activity,MainActivity::class.java)
            activity.startActivity(intent)
        }

    }

}

