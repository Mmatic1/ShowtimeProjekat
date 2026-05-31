package com.example.mobilnaappfilmovi.features.movies.filter

import projekat.features.movies.domain.Filters
import projekat.features.movies.domain.Genre

interface FiltersContract {
    data class UiState(
        val availavleGenres: List<projekat.features.movies.domain.Genre> = emptyList(),
        val selectedFilters: projekat.features.movies.domain.Filters = _root_ide_package_.projekat.features.movies.domain.Filters(),
        val isLoading: Boolean=false,
        val error: String?=null
    )

    sealed class UiEvent {

        data object LoadGenres : UiEvent()

        data class QueryChanged(val query: String) : UiEvent()
        data class GenreSelected(val genre: projekat.features.movies.domain.Genre?) : UiEvent()
        data class YearRangeChanged(val minYear: Int?, val maxYear: Int?) : UiEvent()
        data class MinRatingChanged(val rating: Float) : UiEvent()

        data object ApplyFiltersClicked : UiEvent()
        data object ClearAllClicked : UiEvent()
        data object BackClicked : UiEvent()
    }

    sealed class SideEffect {
        data class ApplyFilters(val filters: projekat.features.movies.domain.Filters) : SideEffect()
        data object NavigateBack : SideEffect()
    }
}