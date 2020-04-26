package com.horse.proud.di

import com.horse.proud.data.TaskRepository
import com.horse.proud.data.network.TaskNetwork
import com.horse.proud.repository.TestRepository
import com.horse.proud.ui.home.TestModelFactory
import com.horse.proud.ui.task.TaskFragmentViewModelFactory
import org.koin.dsl.module

val appModule = module {
    factory{
        TestModelFactory(TestRepository.getInstance())
    }

    factory{
        TaskFragmentViewModelFactory(TaskRepository.getInstance(TaskNetwork.getInstance()))
    }
}