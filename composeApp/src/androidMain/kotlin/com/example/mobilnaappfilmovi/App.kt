package com.example.mobilnaappfilmovi

import android.app.Application
import com.example.mobilnaappfilmovi.di.initKoin

import org.koin.android.ext.koin.androidContext


class App: Application()
{

    override fun onCreate() {
        super.onCreate()
        AppContextHolder.init(this)
        initKoin {
            androidContext(this@App)
        }
    }
}