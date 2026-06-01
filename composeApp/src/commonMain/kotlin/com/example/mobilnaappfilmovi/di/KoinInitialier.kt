package com.example.mobilnaappfilmovi.di

import com.example.mobilnaappfilmovi.core.auth.di.authModule
import com.example.mobilnaappfilmovi.core.database.di.databaseModule
import com.example.mobilnaappfilmovi.features.auth.di.authFeatureModule
import com.example.mobilnaappfilmovi.features.movies.di.moviesModule
import com.example.mobilnaappfilmovi.features.splash.di.splashModule
import com.example.mobilnaappfilmovi.networking.di.networkingModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration



fun initKoin(config: KoinAppDeclaration? = null): KoinApplication {
    return startKoin {
        config?.invoke(this)
        modules(
            moviesModule,
            networkingModule,
            authModule,
            authFeatureModule,
            splashModule,
            databaseModule()
        )
    }}