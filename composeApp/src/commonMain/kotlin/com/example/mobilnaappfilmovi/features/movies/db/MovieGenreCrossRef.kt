package com.example.mobilnaappfilmovi.features.movies.db

import androidx.room.Entity

@Entity(
    tableName = "movie_genre_cross_ref",
    primaryKeys = ["movieId","genreId"]
)
class MovieGenreCrossRef (
    val movieId: String,
    val genreId: Int
)

