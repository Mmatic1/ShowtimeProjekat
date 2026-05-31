package com.example.mobilnaappfilmovi.features.auth.data

import com.example.mobilnaappfilmovi.core.auth.AuthStore
import com.example.mobilnaappfilmovi.features.auth.AuthRepository
import com.example.mobilnaappfilmovi.features.profile.data.toDomain
import com.example.mobilnaappfilmovi.networking.AuthApi
import com.example.mobilnaappfilmovi.networking.UserApi
import com.example.mobilnaappfilmovi.networking.model.authApiModel.LoginApiModel
import com.example.mobilnaappfilmovi.networking.model.authApiModel.SignupApiModel

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val userApi: UserApi,
    private val authStore: AuthStore,
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
        authStore.clearAuthData()
    }

    override suspend fun getCurrentUser() =
        userApi.me().toDomain()

}