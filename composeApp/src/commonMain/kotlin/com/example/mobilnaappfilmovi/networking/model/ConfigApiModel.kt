package com.example.mobilnaappfilmovi.networking.model

import kotlinx.serialization.Serializable

@Serializable
data class ConfigApiModel(
    val key: String,
    val value: String
)

