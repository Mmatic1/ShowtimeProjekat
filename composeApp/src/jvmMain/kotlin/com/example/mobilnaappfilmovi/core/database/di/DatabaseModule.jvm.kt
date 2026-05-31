package com.example.mobilnaappfilmovi.core.database.di
import com.example.mobilnaappfilmovi.core.database.AppDatabase
import com.example.mobilnaappfilmovi.core.database.buildAppDatabase
import com.example.mobilnaappfilmovi.core.database.getDatabaseBuilder

import org.koin.core.module.Module
import org.koin.dsl.module

actual fun databaseModule(): Module = module {

    single<AppDatabase> {
        buildAppDatabase(
            builder = getDatabaseBuilder()
        )
    }

    single {
        get<AppDatabase>().moviesDao()
    }
}