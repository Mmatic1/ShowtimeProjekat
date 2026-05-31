package com.example.mobilnaappfilmovi.features.auth

import com.example.mobilnaappfilmovi.features.profile.domain.User

interface AuthRepository {
    suspend fun login(
        username: String,
        password: String,
    )

    suspend fun signup(
        fullName: String,
        username: String,
        password: String,
    )

    suspend fun logout()
    suspend fun getCurrentUser(): User
}