package com.example.mobilnaappfilmovi.features.movies.filter

import com.example.mobilnaappfilmovi.features.movies.domain.Filters
import com.example.mobilnaappfilmovi.features.movies.domain.Genre


interface FiltersContract {
    data class UiState(
        val availavleGenres: List<Genre> = emptyList(),
        val selectedFilters: Filters = Filters(),
        val isLoading: Boolean=false,
        val error: String?=null
    )

    sealed class UiEvent {

        data object LoadGenres : UiEvent()

        data class QueryChanged(val query: String) : UiEvent()
        data class GenreSelected(val genre: Genre?) : UiEvent()
        data class YearRangeChanged(val minYear: Int?, val maxYear: Int?) : UiEvent()
        data class MinRatingChanged(val rating: Float) : UiEvent()

        data object ApplyFiltersClicked : UiEvent()
        data object ClearAllClicked : UiEvent()
        data object BackClicked : UiEvent()
    }

    sealed class SideEffect {
        data class ApplyFilters(val filters: Filters) : SideEffect()
        data object NavigateBack : SideEffect()
    }
}