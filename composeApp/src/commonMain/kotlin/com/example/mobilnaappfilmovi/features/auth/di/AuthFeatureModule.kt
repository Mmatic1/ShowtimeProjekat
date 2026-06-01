package com.example.mobilnaappfilmovi.features.auth.di

import com.example.mobilnaappfilmovi.features.auth.AuthRepository
import com.example.mobilnaappfilmovi.features.auth.data.AuthRepositoryImpl
import com.example.mobilnaappfilmovi.features.auth.login.LoginViewModel
import com.example.mobilnaappfilmovi.features.auth.signup.SignupViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authFeatureModule = module {

    single<AuthRepository> {
        AuthRepositoryImpl(
            authApi = get(),
            userApi = get(),
            authStore = get(),
        )
    }

    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)
}