package com.example.mobilnaappfilmovi.features.auth.data

import com.example.mobilnaappfilmovi.core.auth.AuthStore
import com.example.mobilnaappfilmovi.core.database.AppDatabase
import com.example.mobilnaappfilmovi.features.auth.AuthRepository
import com.example.mobilnaappfilmovi.features.profile.data.toDomain
import com.example.mobilnaappfilmovi.networking.api.AuthApi
import com.example.mobilnaappfilmovi.networking.api.UserApi
import com.example.mobilnaappfilmovi.networking.model.authApiModel.LoginApiModel
import com.example.mobilnaappfilmovi.networking.model.authApiModel.SignupApiModel

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val userApi: UserApi,
    private val authStore: AuthStore,
    private val database: AppDatabase,
): AuthRepository {
    override suspend fun login(username: String, password: String) {
        val response=authApi.login(
            LoginApiModel(
                username=username,
                password=password,
            )
        )
        authStore.setAccessToken(response.accessToken)
    }

    override suspend fun signup(
        fullName: String,
        username: String,
        password: String
    ) {
        val response=
            authApi.signup(
                SignupApiModel(
                    fullName=fullName,
                    username=username,
                    password=password,
                )
            )
        authStore.setAccessToken(response.accessToken)
    }

    override suspend fun logout() {
        database.moviesDao().clearFavoriteFlags()
        database.moviesDao().clearWatchlistFlags()

        database.userDao().clear()
        authStore.clearAuthData()
    }

    override suspend fun getCurrentUser() =
        userApi.me().toDomain()

}