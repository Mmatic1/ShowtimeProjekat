package com.example.mobilnaappfilmovi.features.movies.domain

data class Movie(
    val id: String,
    val name:String,
    val year:Int,
    val rating: Float,
    val votes:Int,
    val genres: List<Genre>,
    val posterUrl:String
)