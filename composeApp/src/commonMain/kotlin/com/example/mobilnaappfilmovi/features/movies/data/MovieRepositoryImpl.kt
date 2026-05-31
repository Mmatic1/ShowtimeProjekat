package com.example.mobilnaappfilmovi.features.movies.data

import com.example.mobilnaappfilmovi.core.database.AppDatabase
import com.example.mobilnaappfilmovi.features.movies.db.MovieGenreCrossRef
import com.example.mobilnaappfilmovi.features.movies.domain.Filters
import com.example.mobilnaappfilmovi.features.movies.domain.Genre
import com.example.mobilnaappfilmovi.features.movies.domain.Movie
import com.example.mobilnaappfilmovi.features.movies.domain.MovieDetails
import com.example.mobilnaappfilmovi.features.movies.domain.MovieRepository
import com.example.mobilnaappfilmovi.features.movies.domain.SortType
import com.example.mobilnaappfilmovi.features.movies.repository.toApiValue
import com.example.mobilnaappfilmovi.networking.MoviesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class MovieRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val moviesApi: MoviesApi,
) : MovieRepository {

    private var imageBaseUrl = ""
    private var posterSize = ""
    private var backdropSize = ""

    private suspend fun ensureConfigLoaded() {

        if (imageBaseUrl.isNotEmpty()) return

        val config = moviesApi
            .getConfig()
            .associate { it.key to it.value }

        imageBaseUrl =
            config["image_base_url"] ?: ""

        posterSize =
            config["poster_sizes"]
                ?.split(",")
                ?.find { it == "w342" }
                ?: "w342"

        backdropSize =
            config["backdrop_sizes"]
                ?.split(",")
                ?.find { it == "w780" }
                ?: "w780"
    }

    override fun observeMovies(): Flow<List<Movie>> =
        appDatabase
            .moviesDao()
            .observeMovies()
            .distinctUntilChanged()
            .map { rows ->
                rows.map { it.toDomain() }
            }

    override fun observeMovie(id: String): Flow<MovieDetails?> =
        appDatabase
            .moviesDao()
            .observeMovieDetails(id)
            .map { row ->
                row?.toDomain()
            }

    override fun observeGenres(): Flow<List<Genre>> =
        appDatabase
            .moviesDao()
            .observeGenres()
            .map { rows ->
                rows.map { it.toDomain() }
            }

    override suspend fun refreshMovies(
        sort: SortType,
        filters: Filters,
    ) {

        ensureConfigLoaded()

        val response = moviesApi.getMovies(
            sortBy = sort.toApiValue(),
            query = filters.query,
            genreId = filters.genreId,
            minYear = filters.minYear,
            maxYear = filters.maxYear,
            minRating = filters.minRating,
        )

        val movieEntities =
            response.items.map {
                it.toMovieEntity(
                    imageBaseUrl = imageBaseUrl,
                    posterSize = posterSize,
                )
            }

        val genreEntities =
            response.items
                .flatMap { it.genres }
                .distinctBy { it.id }
                .map { it.toGenreEntity() }

        val crossRefs =
            response.items.flatMap { movie ->
                movie.genres.map { genre ->
                    MovieGenreCrossRef(
                        movieId = movie.imdbId,
                        genreId = genre.id,
                    )
                }
            }

        appDatabase
            .moviesDao()
            .refreshMoviesTransaction(
                movies = movieEntities,
                genres = genreEntities,
                crossRefs = crossRefs,
            )
    }

    override suspend fun refreshMovie(id: String) {

        ensureConfigLoaded()

        val model =
            moviesApi.getMovieDetails(id)

        val movieEntity =
            model.toMovieEntity(
                imageBaseUrl = imageBaseUrl,
                posterSize = posterSize,
            )

        val detailsEntity =
            model.toMovieDetailsEntity(
                imageBaseUrl = imageBaseUrl,
                backdropSize = backdropSize,
            )

        val genreEntities =
            model.genres.map {
                it.toGenreEntity()
            }

        val crossRefs =
            model.genres.map {
                MovieGenreCrossRef(
                    movieId = model.imdbId,
                    genreId = it.id,
                )
            }

        appDatabase
            .moviesDao()
            .refreshMovieDetailsTransaction(
                movie = movieEntity,
                details = detailsEntity,
                genres = genreEntities,
                crossRefs = crossRefs,
            )
    }

    override suspend fun refreshGenres() {

        val genres =
            moviesApi.getGenres()

        appDatabase
            .moviesDao()
            .insertGenres(
                genres.map {
                    it.toGenreEntity()
                }
            )
    }
}