package com.example.mobilnaappfilmovi.features.movies.saved

import com.example.mobilnaappfilmovi.features.movies.domain.Movie

interface SavedMoviesContract {

    data class UiState(
        val movies: List<Movie> = emptyList(),
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,
        val error: String? = null,
        val syncMessage: String? = null,
    )

    sealed class UiEvent {
        data object Load : UiEvent()
        data object Retry : UiEvent()
        data object BackClicked : UiEvent()
        data object SyncMessageShown : UiEvent()
        data class MovieClicked(val movieId: String) : UiEvent()
        data class RemoveClicked(val movieId: String) : UiEvent()
    }

    sealed class SideEffect {
        data object NavigateBack : SideEffect()
        data class NavigateToDetails(val movieId: String) : SideEffect()
    }
}
