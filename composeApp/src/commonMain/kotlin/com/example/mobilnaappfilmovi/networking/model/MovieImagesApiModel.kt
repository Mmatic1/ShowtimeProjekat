package com.example.mobilnaappfilmovi.networking.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieImagesApiModel(
    val backdrops: List<ImageItemApiModel> = emptyList(),
    val posters: List<ImageItemApiModel> = emptyList()
)