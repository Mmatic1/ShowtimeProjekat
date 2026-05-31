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
}