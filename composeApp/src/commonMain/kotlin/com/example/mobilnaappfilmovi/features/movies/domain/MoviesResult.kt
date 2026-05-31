package com.example.mobilnaappfilmovi.features.movies.domain

data class MoviesResult(
    val movies:List<Movie>,
    val total: Int
)