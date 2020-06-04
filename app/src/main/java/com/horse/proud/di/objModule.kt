package com.horse.proud.di

import com.horse.proud.data.*
import com.horse.proud.data.network.*
import com.horse.proud.ui.init.SplashActivityViewModel
import com.horse.proud.ui.init.SplashActivityViewModelFactory
import com.horse.proud.ui.login.ForgetPasswordActivityViewModelFactory
import com.horse.proud.ui.login.LoginActivityViewModelFactory
import com.horse.proud.ui.login.RegisterActivityViewModelFactory
import com.horse.proud.ui.lost.FoundActivityViewModelFactory
import com.horse.proud.ui.lost.LostActivityViewModelFactory
import com.horse.proud.ui.lost.LostFragmentViewModelFactory
import com.horse.proud.ui.rental.RentalActivityViewModelFactory
import com.horse.proud.ui.rental.RentalFragmentViewModelFactory
import com.horse.proud.ui.setting.EditPersonalInfoViewModelFactory
import com.horse.proud.ui.task.TaskActivityViewModelFactory
import com.horse.proud.ui.task.TaskFragmentViewModelFactory
import org.koin.dsl.module

val appModule = module {

    factory{
        SplashActivityViewModelFactory(SplashRepository.getInstance(SplashNetWork.getInstance()))
    }

    factory{
        LoginActivityViewModelFactory(LoginRepository.getInstance(LoginNetwork.getInstance()))
    }

    factory{
        RegisterActivityViewModelFactory(LoginRepository.getInstance(LoginNetwork.getInstance()))
    }

    factory{
        EditPersonalInfoViewModelFactory(EditPersonalRepository.getInstance(EditPersonalInfoNetwork.getInstance()))
    }

    factory{
        ForgetPasswordActivityViewModelFactory(LoginRepository.getInstance(LoginNetwork.getInstance()))
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
        LostActivityViewModelFactory(LostRepository.getInstance(LostNetwork.getInstance()))
    }

    factory{
        FoundActivityViewModelFactory(LostRepository.getInstance(LostNetwork.getInstance()))
    }

    factory{
        RentalFragmentViewModelFactory(RentalRepository.getInstance(RentalNetwork.getInstance()))
    }

    factory{
        RentalActivityViewModelFactory(RentalRepository.getInstance(RentalNetwork.getInstance()))
    }

}