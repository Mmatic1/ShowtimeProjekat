package com.example.mobilnaappfilmovi

import android.app.Application
import com.example.mobilnaappfilmovi.core.db.di.androidDatabaseModule
import com.example.mobilnaappfilmovi.di.initKoin
import com.example.mobilnaappfilmovi.features.movies.di.moviesModule
import com.example.mobilnaappfilmovi.networking.di.networkingModule
import org.koin.android.ext.koin.androidContext


class App: Application()
{

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@App)
            modules(
                androidDatabaseModule(this@App),
               moviesModule,
                networkingModule
            )
        }
    }
}