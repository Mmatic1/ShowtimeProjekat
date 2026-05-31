package com.example.mobilnaappfilmovi.features.splash.di

import com.example.mobilnaappfilmovi.features.splash.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val splashModule = module {
    viewModelOf(::SplashViewModel)
}