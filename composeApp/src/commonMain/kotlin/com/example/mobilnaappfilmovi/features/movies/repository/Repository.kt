package com.example.mobilnaappfilmovi.features.movies.repository

import com.example.mobilnaappfilmovi.features.movies.domain.Actor
import com.example.mobilnaappfilmovi.features.movies.domain.Filters
import com.example.mobilnaappfilmovi.features.movies.domain.Genre
import com.example.mobilnaappfilmovi.features.movies.domain.Movie
import com.example.mobilnaappfilmovi.features.movies.domain.MovieDetails
import com.example.mobilnaappfilmovi.features.movies.domain.MovieRepository
import com.example.mobilnaappfilmovi.features.movies.domain.MoviesResult
import com.example.mobilnaappfilmovi.features.movies.domain.SortType
import com.example.mobilnaappfilmovi.networking.MoviesApi
import com.example.mobilnaappfilmovi.networking.model.ActorApiModel
import com.example.mobilnaappfilmovi.networking.model.GenreApiModel
import com.example.mobilnaappfilmovi.networking.model.MovieDetailsApiModel
import com.example.mobilnaappfilmovi.networking.model.MovieListItemApiModel
import kotlinx.coroutines.flow.MutableStateFlow


class Repository(
    private val moviesApi: MoviesApi
): MovieRepository {
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    private var imageBaseUrl: String=""
    private var posterSize: String=""
    private var backdropSize: String=""

    private suspend fun configLoaded()
    {
        if (imageBaseUrl.isNotEmpty())return

        val configList=moviesApi.getConfig()
        val map=configList.associate { it.key to it.value}

        imageBaseUrl = map["image_base_url"]?:""
        posterSize = map["poster_sizes"]
            ?.split(",")
            ?.find { it=="w342" }
            ?: "w342"

        backdropSize=map["backdrop_sizes"]
            ?.split(",")
            ?.find { it=="w780" }
            ?:"w780"
    }

    override suspend fun getGenres(): List<Genre> {
        val response=moviesApi.getGenres()
        return response.map{it.toDomain()}
    }


    override suspend fun getMovies(
        sort: SortType,
        filters: Filters
    ): MoviesResult {

        configLoaded()

        val response = moviesApi.getMovies(
            sortBy = sort.toApiValue(),
            query = filters.query,
            genreId = filters.genreId,
            minYear = filters.minYear,
            maxYear = filters.maxYear,
            minRating = filters.minRating,

        )

        val movies = response.items.map { it.toDomain(imageBaseUrl,posterSize) }

        return MoviesResult(
            movies = movies,
            total = response.totalItems
        )
    }


    override suspend fun getMovieDetails(id: String): MovieDetails {
       configLoaded()
        val detailsApi=moviesApi.getMovieDetails(id)
        val imagesApi=moviesApi.getMovieImages(id)
        val castApi=moviesApi.getMovieCast(id)
        val videosApi=moviesApi.getMovieVideos(id)

        val trailer=videosApi
            .firstOrNull{it.site=="YouTube" && it.key.isNotBlank()}

        val trailerUrl=trailer?.let {
            "https://www.youtube.com/watch?v=${it.key}"
        }

        val imageUrls=imagesApi.backdrops.map {
            imageBaseUrl+backdropSize+it.filePath
        }

        val cast=castApi.items
            .filter { it.department=="Acting" }
            .map { it.toDomain(imageBaseUrl,posterSize) }

        return detailsApi.toDomain(
            imageBaseUrl=imageBaseUrl,
            backdropSize=backdropSize,
            posterSize=posterSize,
            images=imageUrls,
            cast=cast,
            trailerUrl=trailerUrl
        )
    }
}
fun SortType.toApiValue(): String {
    return when (this) {
       SortType.RATING -> "imdb_rating"
        SortType.YEAR -> "year"
       SortType.TITLE -> "title"
        SortType.POPULARITY -> "popularity"
    }
}
fun MovieListItemApiModel.toDomain(
    imageBaseUrl: String,
    posterSize: String
): Movie {
    return Movie(
        id = imdbId,
        name = title,
        year = year ?: 0,
        rating = imdbRating?.toFloat() ?: 0f,
        votes = imdbVotes ?: 0,
        posterUrl = posterPath?.let { imageBaseUrl + posterSize + it } ?: "",
        genres = genres.map { it.toDomain() }
    )
}

fun GenreApiModel.toDomain(): Genre {
    return Genre(
        id = id,
        name = name
    )
}

fun MovieDetailsApiModel.toDomain(
    imageBaseUrl: String,
    backdropSize: String,
    posterSize: String,
    images: List<String>,
    cast:List<Actor>,
    trailerUrl: String?
)= MovieDetails(
    imdbId = imdbId,
    tmdbId = tmdbId,
    title = title,
    originalTitle = originalTitle,
    overview = overview,
    tagline = tagline,
    releaseDate = releaseDate,
    year = year,
    runtime = runtime,
    budget = budget,
    revenue = revenue,
    languageCode = languageCode,
    popularity = popularity,
    imdbRating = imdbRating,
    imdbVotes = imdbVotes,
    tmdbRating = tmdbRating,
    tmdbVotes = tmdbVotes,
    posterPath = posterPath?.let { imageBaseUrl + posterSize + it } ?: "",
    backdropPath = backdropPath?.let { imageBaseUrl + backdropSize + it } ?: "",
    homepage = homepage,
    genres = genres.map { it.toDomain() },
    images = images,
    cast = cast,
    trailerUrl = trailerUrl
)

fun ActorApiModel.toDomain(
    imageBaseUrl: String,
    posterSize: String
): Actor {
    return Actor(
        id = imdbId,
        name = name,
        profileUrl = profilePath?.let {
            imageBaseUrl + posterSize + it
        } ?: ""
    )
}