package com.example.mobilnaappfilmovi.core.auth.di

import androidx.datastore.core.DataStore
import com.example.mobilnaappfilmovi.core.auth.AuthStore
import com.example.mobilnaappfilmovi.core.auth.createAuthDataStore
import com.example.mobilnaappfilmovi.core.auth.model.AuthData
import org.koin.dsl.module

val authModule = module {

    single<DataStore<AuthData>> { createAuthDataStore() }

    single<AuthStore> { AuthStore(persistence = get()) }
}