package com.horse.proud.di

import com.horse.proud.data.LoginRepository
import com.horse.proud.data.LostRepository
import com.horse.proud.data.RentalRepository
import com.horse.proud.data.TaskRepository
import com.horse.proud.data.network.LoginNetwork
import com.horse.proud.data.network.LostNetwork
import com.horse.proud.data.network.RentalNetwork
import com.horse.proud.data.network.TaskNetwork
import com.horse.proud.ui.login.LoginActivityViewModelFactory
import com.horse.proud.ui.login.RegisterActivityViewModelFactory
import com.horse.proud.ui.lost.LostFragmentViewModelFactory
import com.horse.proud.ui.rental.RentalFragmentViewModelFactory
import com.horse.proud.ui.task.TaskActivityViewModelFactory
import com.horse.proud.ui.task.TaskFragmentViewModelFactory
import org.koin.dsl.module

val appModule = module {

    factory{
        LoginActivityViewModelFactory(LoginRepository.getInstance(LoginNetwork.getInstance()))
    }

    factory{
        RegisterActivityViewModelFactory(LoginRepository.getInstance(LoginNetwork.getInstance()))
    }

    factory{
        TaskFragmentViewModelFactory(TaskRepository.getInstance(TaskNetwork.getInstance()))
    }

    factory{
        TaskActivityViewModelFactory(TaskRepository.getInstance(TaskNetwork.getInstance()))
    }

    factory{
        LostFragmentViewModelFactory(LostRepository.getInstance(LostNetwork.getInstance()))
    }

    factory{
        RentalFragmentViewModelFactory(RentalRepository.getInstance(RentalNetwork.getInstance()))
    }

}