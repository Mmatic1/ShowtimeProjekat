package com.example.mobilnaappfilmovi.features.profile.domain

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeUser(): Flow<User?>
    suspend fun refreshUser()
}