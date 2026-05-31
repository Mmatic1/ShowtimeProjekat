package com.example.mobilnaappfilmovi.features.profile.data

import com.example.mobilnaappfilmovi.core.database.AppDatabase
import com.example.mobilnaappfilmovi.features.profile.domain.User
import com.example.mobilnaappfilmovi.features.profile.domain.UserRepository
import com.example.mobilnaappfilmovi.networking.UserApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl (
    private val appDatabase: AppDatabase,
    private val userApi: UserApi,
): UserRepository{
    override fun observeUser(): Flow<User> =
        appDatabase
            .userDao()
            .observeUser()
            .map { entity ->
                entity?.toDomain()
                    ?: User(
                        id = 0,
                        username = "",
                        fullName = "",
                    )
            }

    override suspend fun refreshUser() {

        val user =
            userApi.me()

        appDatabase
            .userDao()
            .insertUser(
                user.toEntity()
            )
    }
}