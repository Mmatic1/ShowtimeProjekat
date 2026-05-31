package com.example.mobilnaappfilmovi.networking.model.authApiModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserApiModel(
    val id: Long,

    val username: String,

    @SerialName("full_name")
    val fullName: String,
)
