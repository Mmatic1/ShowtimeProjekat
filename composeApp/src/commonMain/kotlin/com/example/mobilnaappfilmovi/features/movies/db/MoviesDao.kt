package com.example.mobilnaappfilmovi.features.movies.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {


    @Transaction
    @Query("SELECT * FROM movies")
    fun observeMovies(): Flow<List<MovieWithGenres>>

    @Transaction
    @Query("SELECT * FROM movies WHERE imdbId = :movieId")
    fun observeMovieDetails(movieId: String): Flow<MovieDetailsWithGenres?>
    @Transaction
    @Query("SELECT * FROM movies WHERE imdbId = :movieId")
    fun observeMovie(movieId: String): Flow<MovieWithGenres?>

    @Query("SELECT * FROM genres ORDER BY name ASC")
    fun observeGenres(): Flow<List<GenreEntity>>

    @Transaction
    @Query("SELECT * FROM movies WHERE favorite = 1")
    fun observeFavorites(): Flow<List<MovieWithGenres>>

    @Transaction
    @Query("SELECT * FROM movies WHERE watchlist = 1")
    fun observeWatchlist(): Flow<List<MovieWithGenres>>

    @Transaction
    @Query("""
        SELECT * FROM movies
        WHERE title LIKE '%' || :query || '%'
    """)
    fun searchMovies(query: String): Flow<List<MovieWithGenres>>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(
        movies: List<MovieEntity>,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(
        genres: List<GenreEntity>,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieGenreCrossRefs(
        refs: List<MovieGenreCrossRef>,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetails(
        details: MovieDetailsEntity,
    )



    @Query("""
        UPDATE movies
        SET favorite = :isFavorite
        WHERE imdbId = :movieId
    """)
    suspend fun updateFavorite(
        movieId: String,
        isFavorite: Boolean,
    )

    @Query("""
        UPDATE movies
        SET watchlist = :inWatchlist
        WHERE imdbId = :movieId
    """)
    suspend fun updateWatchlist(
        movieId: String,
        inWatchlist: Boolean,
    )



    @Query("DELETE FROM movies")
    suspend fun clearMovies()

    @Query("DELETE FROM movie_genre_cross_ref")
    suspend fun clearMovieGenreCrossRefs()



    @Transaction
    suspend fun refreshMoviesTransaction(
        movies: List<MovieEntity>,
        genres: List<GenreEntity>,
        crossRefs: List<MovieGenreCrossRef>,
    ) {
        clearMovies()
        clearMovieGenreCrossRefs()

        insertMovies(movies)
        insertGenres(genres)
        insertMovieGenreCrossRefs(crossRefs)
    }

    @Transaction
    suspend fun refreshMovieDetailsTransaction(
        movie: MovieEntity,
        details: MovieDetailsEntity,
        genres: List<GenreEntity>,
        crossRefs: List<MovieGenreCrossRef>,
    ) {
        insertMovies(listOf(movie))
        insertMovieDetails(details)
        insertGenres(genres)
        insertMovieGenreCrossRefs(crossRefs)
    }
}