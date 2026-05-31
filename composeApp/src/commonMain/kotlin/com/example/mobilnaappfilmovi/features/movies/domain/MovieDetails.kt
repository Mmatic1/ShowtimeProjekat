package com.example.mobilnaappfilmovi.features.movies.domain

data class MovieDetails(
    val imdbId: String,
    val tmdbId: Int?,
    val title: String,
    val originalTitle: String?,
    val overview: String?,
    val tagline: String?,
    val releaseDate: String?,
    val year: Int?,
    val runtime: Int?,
    val budget: Long?,
    val revenue: Long?,
    val languageCode: String?,
    val popularity: Float?,
    val imdbRating: Float?,
    val imdbVotes: Int?,
    val tmdbRating: Float?,
    val tmdbVotes: Int?,
    val posterPath: String?,
    val backdropPath: String?,
    val homepage: String?,
    val genres: List<Genre>,
    val images: List<String>,
    val cast:List<Actor> = emptyList(),
    val trailerUrl: String?=null
)