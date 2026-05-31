package com.example.mobilnaappfilmovi.features.movies.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_details")
data class MovieDetailsEntity(

    @PrimaryKey
    val imdbId: String,
    val tmdbId: Int?,
    val originalTitle: String?,
    val overview: String?,
    val tagline: String?,
    val releaseDate: String?,
    val runtime: Int?,
    val budget: Long?,
    val revenue: Long?,
    val languageCode: String?,
    val popularity: Float?,
    val tmdbRating: Float?,
    val tmdbVotes: Int?,
    val backdropUrl: String?,
    val homepage: String?,
)