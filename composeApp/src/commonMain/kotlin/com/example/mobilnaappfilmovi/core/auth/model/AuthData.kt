package com.example.mobilnaappfilmovi.core.auth.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class AuthData(
    val accessToken: String?=null,
    val refreshToken: String?=null,
){
    companion object {
        fun empty(): AuthData = AuthData(
            accessToken = "",
            refreshToken = "",
        )
    }
}
