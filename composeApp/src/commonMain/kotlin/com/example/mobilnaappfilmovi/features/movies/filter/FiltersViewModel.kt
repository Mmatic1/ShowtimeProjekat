package com.example.mobilnaappfilmovi.features.movies.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import projekat.features.movies.domain.Filters
import projekat.features.movies.domain.MovieRepository

class FiltersViewModel(
    private val movieRepository: projekat.features.movies.domain.MovieRepository
): ViewModel() {
    private val _state=MutableStateFlow(_root_ide_package_.projekat.features.movies.filter.FiltersContract.UiState())
    val state=_state.asStateFlow()

    private val _effects=MutableSharedFlow<projekat.features.movies.filter.FiltersContract.SideEffect>()
    val effects=_effects.asSharedFlow()

    private fun setState(reducer: projekat.features.movies.filter.FiltersContract.UiState.()-> projekat.features.movies.filter.FiltersContract.UiState)
    {
        _state.getAndUpdate(reducer)
    }

    private fun setEffect(effect: projekat.features.movies.filter.FiltersContract.SideEffect)
    {
        viewModelScope.launch { _effects.emit(effect)}
    }

    init {
        fetchGenres()
    }

    fun onEvent(event: projekat.features.movies.filter.FiltersContract.UiEvent)
    {
        when(event){
            is projekat.features.movies.filter.FiltersContract.UiEvent.LoadGenres -> fetchGenres()

            is projekat.features.movies.filter.FiltersContract.UiEvent.QueryChanged -> {
                setState {
                    _root_ide_package_.projekat.features.movies.filter.FiltersContract.UiState(
                        selectedFilters = selectedFilters.copy(query = event.query)
                    )
                }
            }

            is projekat.features.movies.filter.FiltersContract.UiEvent.GenreSelected -> {
                setState {
                    _root_ide_package_.projekat.features.movies.filter.FiltersContract.UiState(
                        selectedFilters = selectedFilters.copy(genreId = event.genre?.id)
                    )
                }

            }

            is projekat.features.movies.filter.FiltersContract.UiEvent.YearRangeChanged -> {
                setState {
                    _root_ide_package_.projekat.features.movies.filter.FiltersContract.UiState(
                        selectedFilters = selectedFilters.copy(
                            minYear = event.minYear,
                            maxYear = event.maxYear
                        )
                    )
                }
            }

            is projekat.features.movies.filter.FiltersContract.UiEvent.MinRatingChanged -> {
                setState {
                    _root_ide_package_.projekat.features.movies.filter.FiltersContract.UiState(
                        selectedFilters = selectedFilters.copy(minRating = event.rating)
                    )
                }
            }

            is projekat.features.movies.filter.FiltersContract.UiEvent.ClearAllClicked -> {
                setState {
                    _root_ide_package_.projekat.features.movies.filter.FiltersContract.UiState(
                        selectedFilters = _root_ide_package_.projekat.features.movies.domain.Filters()
                    )
                }
            }

            is projekat.features.movies.filter.FiltersContract.UiEvent.ApplyFiltersClicked -> {
                setEffect(_root_ide_package_.projekat.features.movies.filter.FiltersContract.SideEffect.ApplyFilters(state.value.selectedFilters))
            }

            is projekat.features.movies.filter.FiltersContract.UiEvent.BackClicked -> {
                setEffect(_root_ide_package_.projekat.features.movies.filter.FiltersContract.SideEffect.NavigateBack)
            }

        }
    }

    private fun fetchGenres()
    {
        viewModelScope.launch {
            setState {
                _root_ide_package_.projekat.features.movies.filter.FiltersContract.UiState(
                    isLoading = true,
                    error = null
                )
            }

            val result=runCatching {
                movieRepository.getGenres()
            }

            setState {
                _root_ide_package_.projekat.features.movies.filter.FiltersContract.UiState(
                    availavleGenres = result.getOrDefault(emptyList()),
                    isLoading = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }
}