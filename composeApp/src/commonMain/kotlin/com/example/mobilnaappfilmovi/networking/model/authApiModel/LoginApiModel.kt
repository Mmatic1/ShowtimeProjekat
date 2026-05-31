package com.example.mobilnaappfilmovi.networking.model.authApiModel

import kotlinx.serialization.Serializable

@Serializable
data class LoginApiModel(
    val username: String,
    val password: String,
)
