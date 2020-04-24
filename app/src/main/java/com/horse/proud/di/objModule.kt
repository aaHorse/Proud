package com.horse.proud.di

import com.horse.proud.repository.TestRepository
import com.horse.proud.ui.home.TestModelFactory
import org.koin.dsl.module

val appModule = module {//里面添加各种注入对象
    factory{
        TestModelFactory(TestRepository())
    }
}