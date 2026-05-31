package com.example.mobilnaappfilmovi.networking.model

import kotlinx.serialization.Serializable

@Serializable
data class GenreApiModel (
    val id:Int,
    val name:String
)