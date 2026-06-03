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

    @Query("SELECT favorite FROM movies WHERE imdbId = :movieId")
    suspend fun isFavorite(movieId: String): Boolean?

    @Query("SELECT watchlist FROM movies WHERE imdbId = :movieId")
    suspend fun isWatchlist(movieId: String): Boolean?

    @Query("UPDATE movies SET favorite = :value WHERE imdbId = :movieId")
    suspend fun updateFavorite(movieId: String, value: Boolean)

    @Query("UPDATE movies SET watchlist = :value WHERE imdbId = :movieId")
    suspend fun updateWatchlist(movieId: String, value: Boolean)

    @Query("SELECT imdbId FROM movies WHERE favorite = 1")
    suspend fun getFavoriteIds(): List<String>

    @Query("SELECT imdbId FROM movies WHERE watchlist = 1")
    suspend fun getWatchlistIds(): List<String>

    @Query("UPDATE movies SET favorite = 0")
    suspend fun clearFavoriteFlags()

    @Query("UPDATE movies SET watchlist = 0")
    suspend fun clearWatchlistFlags()

    @Query("UPDATE movies SET favorite = 1 WHERE imdbId IN (:movieIds)")
    suspend fun markFavorites(movieIds: List<String>)

    @Query("UPDATE movies SET watchlist = 1 WHERE imdbId IN (:movieIds)")
    suspend fun markWatchlist(movieIds: List<String>)


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
        val favoriteIds = getFavoriteIds().toSet()
        val watchlistIds = getWatchlistIds().toSet()
        val moviesWithUserFlags = movies.map { movie ->
            movie.copy(
                favorite = movie.imdbId in favoriteIds,
                watchlist = movie.imdbId in watchlistIds,
            )
        }

        clearMovies()
        clearMovieGenreCrossRefs()

        insertMovies(moviesWithUserFlags)
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
        val favorite = isFavorite(movie.imdbId) ?: movie.favorite
        val watchlist = isWatchlist(movie.imdbId) ?: movie.watchlist
        insertMovies(listOf(movie.copy(favorite = favorite, watchlist = watchlist)))
        insertMovieDetails(details)
        insertGenres(genres)
        insertMovieGenreCrossRefs(crossRefs)
    }

    @Transaction
    suspend fun replaceFavoritesTransaction(
        movies: List<MovieEntity>,
        genres: List<GenreEntity>,
        crossRefs: List<MovieGenreCrossRef>,
    ) {
        val watchlistIds = getWatchlistIds().toSet()
        insertMovies(
            movies.map { movie ->
                movie.copy(watchlist = movie.imdbId in watchlistIds)
            }
        )
        insertGenres(genres)
        insertMovieGenreCrossRefs(crossRefs)
        clearFavoriteFlags()
        if (movies.isNotEmpty()) {
            markFavorites(movies.map { it.imdbId })
        }
    }

    @Transaction
    suspend fun replaceWatchlistTransaction(
        movies: List<MovieEntity>,
        genres: List<GenreEntity>,
        crossRefs: List<MovieGenreCrossRef>,
    ) {
        val favoriteIds = getFavoriteIds().toSet()
        insertMovies(
            movies.map { movie ->
                movie.copy(favorite = movie.imdbId in favoriteIds)
            }
        )
        insertGenres(genres)
        insertMovieGenreCrossRefs(crossRefs)
        clearWatchlistFlags()
        if (movies.isNotEmpty()) {
            markWatchlist(movies.map { it.imdbId })
        }
    }
}
