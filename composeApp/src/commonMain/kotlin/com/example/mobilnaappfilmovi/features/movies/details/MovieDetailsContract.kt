package com.example.mobilnaappfilmovi.features.movies.details

import com.example.mobilnaappfilmovi.features.movies.domain.MovieDetails


interface MovieDetailsContract{

    data class UiState(
        val movieDetails: MovieDetails?=null,
        val isLoading: Boolean=false,
        val error: String?=null,
    )

    sealed class UiEvent{
        data object LoadDetails: UiEvent()
        data object BackClicked: UiEvent()
    }

    sealed class SideEffect{
        data object NavigateBack: SideEffect()
    }
}