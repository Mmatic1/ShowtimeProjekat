package com.example.mobilnaappfilmovi.features.movies.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilnaappfilmovi.core.auth.AuthStore
import com.example.mobilnaappfilmovi.features.movies.domain.MovieRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MoviesListViewModel(
    private val movieRepository: MovieRepository,
    private val authStore: AuthStore,
) : ViewModel() {

    private val _state =
        MutableStateFlow(MoviesListContract.UiState())

    val state = _state.asStateFlow()

    private val _effects =
        MutableSharedFlow<MoviesListContract.SideEffect>()

    val effects = _effects.asSharedFlow()

    init {
        observeMovies()
        refreshMovies()
    }

    private fun setState(
        reducer: MoviesListContract.UiState.() -> MoviesListContract.UiState
    ) {
        _state.getAndUpdate(reducer)
    }

    private fun setEffect(
        effect: MoviesListContract.SideEffect
    ) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }

    fun onEvent(event: MoviesListContract.UiEvent) {
        when (event) {

            is MoviesListContract.UiEvent.LoadMovies -> {
                refreshMovies()
            }

            is MoviesListContract.UiEvent.Retry -> {
                refreshMovies()
            }

            is MoviesListContract.UiEvent.SortClicked -> {
                setState {
                    copy(showSortMenu = true)
                }
            }

            is MoviesListContract.UiEvent.SortDismissed -> {
                setState {
                    copy(showSortMenu = false)
                }
            }

            is MoviesListContract.UiEvent.ChangeSort -> {

                setState {
                    copy(
                        sort = event.sort,
                        showSortMenu = false,
                        isLoading = true
                    )
                }

                refreshMovies()
            }

            is MoviesListContract.UiEvent.ApplyFilters -> {

                setState {
                    copy(
                        filters = event.filters,
                        isLoading = true
                    )
                }

                refreshMovies()
            }

            is MoviesListContract.UiEvent.MovieClicked -> {
                setEffect(
                    MoviesListContract.SideEffect.NavigateToDetails(
                        event.movieId
                    )
                )
            }

            is MoviesListContract.UiEvent.FilterClicked -> {
                setEffect(
                    MoviesListContract.SideEffect.NavigateToFilters
                )
            }

        }
    }


    private fun observeMovies() {

        viewModelScope.launch {

            movieRepository
                .observeMovies()
                .collectLatest { movies ->

                    setState {
                        copy(
                            movies = movies,
                            totalMovies = movies.size,
                            isLoading = false,
                            isEmpty = movies.isEmpty(),
                            error = null
                        )
                    }
                }
        }
    }

    private fun refreshMovies(){
        viewModelScope.launch {
            val hasMovies=state.value.movies.isNotEmpty()
            setState {
                copy(
                    isLoading=!hasMovies,
                    isRefreshing = hasMovies,
                    error = null
                )
            }
            runCatching {
                movieRepository.refreshMovies(
                    sort = state.value.sort,
                    filters = state.value.filters
                )
            }.onSuccess {
                setState {
                    copy(isLoading=false, isRefreshing = false)
                }
            }.onFailure { exception ->
                setState {
                    copy(
                        isLoading=false,
                        isRefreshing = false,
                        error = exception.message
                            ?:"Nepoznata greska"
                    )
                }
            }
        }
    }
}