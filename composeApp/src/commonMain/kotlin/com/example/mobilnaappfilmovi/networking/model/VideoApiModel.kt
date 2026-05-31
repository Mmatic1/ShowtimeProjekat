package com.example.mobilnaappfilmovi.networking.model

import kotlinx.serialization.Serializable

@Serializable
data class VideoApiModel(
    val key: String,
    val site: String,
    val name: String?=null,
    val type: String?=null,
    val official: Boolean=false
) {
}