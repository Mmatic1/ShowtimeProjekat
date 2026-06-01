package com.example.mobilnaappfilmovi.features.movies.domain

import kotlinx.coroutines.flow.Flow

interface MovieRepository{

    fun observeMovies(): Flow<List<Movie>>

    fun observeMovie(id: String): Flow<MovieDetails?>

    fun observeGenres(): Flow<List<Genre>>

    suspend fun refreshMovies(
        sort: SortType,
        filters: Filters,
    )

    suspend fun refreshMovie(id: String)

    suspend fun refreshGenres()

    suspend fun updateFavorite(movieId: String, value: Boolean)
    suspend fun updateWatchlist(movieId: String, value: Boolean)
    suspend fun addFavorite(movieId: String)
    suspend fun removeFavorite(movieId: String)
    suspend fun addToWatchlist(movieId: String)
    suspend fun removeFromWatchlist(movieId: String)
}