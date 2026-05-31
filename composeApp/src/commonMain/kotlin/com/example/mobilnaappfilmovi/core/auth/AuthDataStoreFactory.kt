package com.example.mobilnaappfilmovi.core.auth

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import com.example.mobilnaappfilmovi.core.auth.model.AuthData
import okio.FileSystem
import okio.Path.Companion.toPath

expect fun createAuthDataStorePath(): String

fun createAuthDataStore(): DataStore<AuthData> {
    return DataStoreFactory.create(
        storage = OkioStorage(
            fileSystem = FileSystem.SYSTEM,
            serializer = AuthDataSerializer,
            producePath = { createAuthDataStorePath().toPath() },
        ),
    )
}