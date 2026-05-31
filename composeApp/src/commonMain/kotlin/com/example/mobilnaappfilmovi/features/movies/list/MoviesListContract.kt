package com.example.mobilnaappfilmovi.features.movies.list

import com.example.mobilnaappfilmovi.features.movies.domain.Filters
import com.example.mobilnaappfilmovi.features.movies.domain.Movie
import com.example.mobilnaappfilmovi.features.movies.domain.SortType


interface MoviesListContract {

    data class UiState(

        val movies: List<Movie> = emptyList(),
        val totalMovies: Int = 0,

        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,
        val isEmpty: Boolean = false,

        val error: String? = null,

        val sort: SortType = SortType.RATING,
        val filters: Filters = Filters(),

        val showSortMenu: Boolean = false
    )

    sealed interface UiEvent {

        data object LoadMovies : UiEvent

        data object Retry : UiEvent

        data object SortClicked : UiEvent

        data object SortDismissed : UiEvent

        data class ChangeSort(
            val sort: SortType
        ) : UiEvent


        data object FilterClicked : UiEvent

        data class ApplyFilters(
            val filters: Filters
        ) : UiEvent


        data class MovieClicked(
            val movieId: String
        ) : UiEvent
    }

    sealed interface SideEffect {

        data class NavigateToDetails(
            val movieId: String
        ) : SideEffect

        data object NavigateToFilters : SideEffect

        data class ShowError(
            val message: String
        ) : SideEffect
    }
}