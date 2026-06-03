package com.example.mobilnaappfilmovi.features.movies.di

import com.example.mobilnaappfilmovi.features.movies.data.MovieRepositoryImpl
import com.example.mobilnaappfilmovi.features.movies.details.MovieDetailsViewModel
import com.example.mobilnaappfilmovi.features.movies.domain.MovieRepository
//import com.example.mobilnaappfilmovi.features.movies.filter.FiltersViewModel
import com.example.mobilnaappfilmovi.features.movies.list.MoviesListViewModel
import com.example.mobilnaappfilmovi.features.movies.saved.FavoriteMoviesViewModel
import com.example.mobilnaappfilmovi.features.movies.saved.WatchlistMoviesViewModel
import com.example.mobilnaappfilmovi.features.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val moviesModule= module {
    single { MovieRepositoryImpl(appDatabase = get(), moviesApi = get()) } bind MovieRepository::class

    viewModelOf(::MoviesListViewModel)
    viewModelOf(::MovieDetailsViewModel)
    //viewModelOf(::FiltersViewModel)
    viewModelOf(::FavoriteMoviesViewModel)
    viewModelOf(::WatchlistMoviesViewModel)
    viewModelOf(::ProfileViewModel)
    }
