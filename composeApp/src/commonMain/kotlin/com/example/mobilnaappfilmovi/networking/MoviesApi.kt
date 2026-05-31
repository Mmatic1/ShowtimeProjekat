package com.example.mobilnaappfilmovi.networking

import com.example.mobilnaappfilmovi.networking.model.ActorApiModel
import com.example.mobilnaappfilmovi.networking.model.ConfigApiModel
import com.example.mobilnaappfilmovi.networking.model.GenreApiModel
import com.example.mobilnaappfilmovi.networking.model.MovieDetailsApiModel
import com.example.mobilnaappfilmovi.networking.model.MovieImagesApiModel
import com.example.mobilnaappfilmovi.networking.model.MovieListItemApiModel
import com.example.mobilnaappfilmovi.networking.model.PaginatedResponse
import com.example.mobilnaappfilmovi.networking.model.VideoApiModel
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import de.jensklingenberg.ktorfit.http.Path

interface MoviesApi {
    @GET("movies")
    suspend fun getMovies(
        @Query("page") page:Int=1,
        @Query("page_size")pageSize: Int=30,
        @Query("query")query: String?=null,
        @Query("genre_id")genreId: Int?=null,
        @Query("min_year")minYear: Int?=null,
        @Query("max_year")maxYear:Int?=null,
        @Query("min_rating")minRating: Float?=null,
        @Query("sort_by")sortBy:String="imdb_votes",
        @Query("sort_order")sortOrder:String="desc"
    ): PaginatedResponse<MovieListItemApiModel>

    @GET("movies/{id}")
    suspend fun getMovieDetails(@Path("id")id: String): MovieDetailsApiModel
    @GET("config")
    suspend fun getConfig(): List<ConfigApiModel>

    @GET("movies/{id}/images")
    suspend fun getMovieImages(@Path("id")id: String): MovieImagesApiModel

    @GET("movies/{id}/cast")
    suspend fun getMovieCast(
        @Path("id")id: String,
        @Query("page_size")pageSize: Int=20
    ): PaginatedResponse<ActorApiModel>

    @GET("movies/{id}/videos")
    suspend fun getMovieVideos(
        @Path("id")id: String,
        @Query("type")type: String="Trailer"
    ): List<VideoApiModel>

    @GET("genres")
    suspend fun getGenres(): List<GenreApiModel>
}