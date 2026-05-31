package com.example.mobilnaappfilmovi.networking.model.authApiModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseApiModel(
    @SerialName("access_token")
    val accessToken: String,

    @SerialName("expires_in")
    val expiresIn: Long,

    val user: UserApiModel,
)
