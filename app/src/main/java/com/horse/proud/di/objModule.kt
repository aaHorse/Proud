package com.horse.proud.di

import com.horse.proud.repository.TestRepository
import com.horse.proud.ui.home.TestModelFactory
import com.horse.proud.ui.task.TaskFragmentViewModel
import org.koin.dsl.module

val appModule = module {
    factory{
        TestModelFactory(TestRepository.getInstance())
    }

    factory{
        TaskFragmentViewModel()
    }
}