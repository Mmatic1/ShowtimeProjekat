package com.example.mobilnaappfilmovi.features.movies.data

import com.example.mobilnaappfilmovi.features.movies.db.GenreEntity
import com.example.mobilnaappfilmovi.features.movies.db.MovieDetailsEntity
import com.example.mobilnaappfilmovi.features.movies.db.MovieDetailsWithGenres
import com.example.mobilnaappfilmovi.features.movies.db.MovieEntity
import com.example.mobilnaappfilmovi.features.movies.db.MovieWithGenres
import com.example.mobilnaappfilmovi.features.movies.domain.Genre
import com.example.mobilnaappfilmovi.features.movies.domain.Movie
import com.example.mobilnaappfilmovi.features.movies.domain.MovieDetails
import com.example.mobilnaappfilmovi.features.movies.domain.SortType
import com.example.mobilnaappfilmovi.networking.model.GenreApiModel
import com.example.mobilnaappfilmovi.networking.model.MovieDetailsApiModel
import com.example.mobilnaappfilmovi.networking.model.MovieListItemApiModel

fun MovieListItemApiModel.toMovieEntity(
    imageBaseUrl: String,
    posterSize: String,
    favorite: Boolean = false,
    watchlist: Boolean = false,
): MovieEntity =
    MovieEntity(
        imdbId = imdbId,
        title = title,
        year = year,
        imdbRating = imdbRating,
        imdbVotes = imdbVotes,
        posterUrl = posterPath?.let {
            "$imageBaseUrl$posterSize$it"
        },
        favorite = favorite,
        watchlist = watchlist,
    )

fun MovieDetailsApiModel.toMovieEntity(
    imageBaseUrl: String,
    posterSize: String,
    favorite: Boolean = false,
    watchlist: Boolean = false,
): MovieEntity =
    MovieEntity(
        imdbId = imdbId,
        title = title,
        year = year,
        imdbRating = imdbRating,
        imdbVotes = imdbVotes,
        posterUrl = posterPath?.let {
            "$imageBaseUrl$posterSize$it"
        },
        favorite = favorite,
        watchlist = watchlist,
    )

fun MovieDetailsApiModel.toMovieDetailsEntity(
    imageBaseUrl: String,
    backdropSize: String,
): MovieDetailsEntity =
    MovieDetailsEntity(
        imdbId = imdbId,
        tmdbId = tmdbId,
        originalTitle = originalTitle,
        overview = overview,
        tagline = tagline,
        releaseDate = releaseDate,
        runtime = runtime,
        budget = budget,
        revenue = revenue,
        languageCode = languageCode,
        popularity = popularity,
        tmdbRating = tmdbRating,
        tmdbVotes = tmdbVotes,
        backdropUrl = backdropPath?.let {
            "$imageBaseUrl$backdropSize$it"
        },
        homepage = homepage,
    )

fun GenreApiModel.toGenreEntity(): GenreEntity =
    GenreEntity(
        id = id,
        name = name,
    )


fun GenreEntity.toDomain(): Genre =
    Genre(
        id = id,
        name = name,
    )

fun MovieEntity.toDomain(
    genres: List<Genre>,
): Movie =
    Movie(
        id = imdbId,
        name = title,
        year = year ?: 0,
        rating = imdbRating ?: 0f,
        votes = imdbVotes ?: 0,
        genres = genres,
        posterUrl = posterUrl ?: "",
    )

fun MovieWithGenres.toDomain(): Movie =
    movie.toDomain(
        genres = genres.map { it.toDomain() }
    )

fun MovieDetailsWithGenres.toDomain(): MovieDetails =
    MovieDetails(
        imdbId = movie.imdbId,
        tmdbId = details?.tmdbId,
        title = movie.title,
        originalTitle = details?.originalTitle,
        overview = details?.overview,
        tagline = details?.tagline,
        releaseDate = details?.releaseDate,
        year = movie.year,
        runtime = details?.runtime,
        budget = details?.budget,
        revenue = details?.revenue,
        languageCode = details?.languageCode,
        popularity = details?.popularity,
        imdbRating = movie.imdbRating,
        imdbVotes = movie.imdbVotes,
        tmdbRating = details?.tmdbRating,
        tmdbVotes = details?.tmdbVotes,
        posterPath = movie.posterUrl,
        backdropPath = details?.backdropUrl,
        homepage = details?.homepage,
        genres = genres.map { it.toDomain() },
        images = emptyList(),
        cast = emptyList(),
        trailerUrl = null,
        favorite=movie.favorite,
        watchlist = movie.watchlist
    )
fun SortType.toApiValue(): String =
    when (this) {
        SortType.RATING -> "rating"
        SortType.YEAR -> "year"
        SortType.TITLE -> "title"
        SortType.POPULARITY -> "popularity"
    }
