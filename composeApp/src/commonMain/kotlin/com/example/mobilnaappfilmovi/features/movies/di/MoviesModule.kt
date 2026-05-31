package com.example.mobilnaappfilmovi.features.movies.di

import com.example.mobilnaappfilmovi.features.movies.details.MovieDetailsViewModel
import com.example.mobilnaappfilmovi.features.movies.domain.MovieRepository
import com.example.mobilnaappfilmovi.features.movies.filter.FiltersViewModel
import com.example.mobilnaappfilmovi.features.movies.list.MoviesListViewModel
import com.example.mobilnaappfilmovi.features.movies.repository.Repository
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import projekat.features.movies.details.MovieDetailsViewModel
import projekat.features.movies.domain.MovieRepository
import projekat.features.movies.filter.FiltersViewModel
import projekat.features.movies.list.MoviesListViewModel
import projekat.features.movies.repository.Repository

val moviesModule= module {
    single { Repository(get()) } bind MovieRepository::class

    viewModelOf(::MoviesListViewModel)
    viewModelOf(::MovieDetailsViewModel)
    viewModelOf(::FiltersViewModel)
    }
