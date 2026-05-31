package com.example.mobilnaappfilmovi

import android.app.Application
import com.example.mobilnaappfilmovi.core.db.di.androidDatabaseModule
import org.koin.android.ext.koin.androidContext
import projekat.di.initKoin
import projekat.features.movies.di.moviesModule
import projekat.core.networking.di.networkingModule

class App: Application()
{

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@App)
            modules(
                androidDatabaseModule(this@App),
                projekat.features.movies.di.moviesModule,
                networkingModule
            )
        }
    }
}