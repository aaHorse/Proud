package com.horse.proud.di

import com.horse.proud.data.LostRepository
import com.horse.proud.data.RentalRepository
import com.horse.proud.data.TaskRepository
import com.horse.proud.data.network.LostNetwork
import com.horse.proud.data.network.RentalNetwork
import com.horse.proud.data.network.TaskNetwork
import com.horse.proud.ui.lost.LostFragmentViewModelFactory
import com.horse.proud.ui.rental.RentalFragmentViewModelFactory
import com.horse.proud.ui.task.TaskFragmentViewModelFactory
import org.koin.dsl.module

val appModule = module {

    factory{
        TaskFragmentViewModelFactory(TaskRepository.getInstance(TaskNetwork.getInstance()))
    }

    factory{
        LostFragmentViewModelFactory(LostRepository.getInstance(LostNetwork.getInstance()))
    }

    factory{
        RentalFragmentViewModelFactory(RentalRepository.getInstance(RentalNetwork.getInstance()))
    }

}