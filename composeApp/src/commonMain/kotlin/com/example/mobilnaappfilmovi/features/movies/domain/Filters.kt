package com.example.mobilnaappfilmovi.features.movies.domain

data class Filters(
    val query: String? = null,
    val genreId: Int? = null,
    val minYear: Int? = null,
    val maxYear: Int? = null,
    val minRating: Float? = null
)