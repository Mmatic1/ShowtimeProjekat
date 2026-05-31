package com.example.mobilnaappfilmovi.networking.model.authApiModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupApiModel(
    @SerialName("full_name")
    val fullName: String,

    val username: String,

    val password: String,
)
