package com.example.mobilnaappfilmovi.features.movies.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilnaappfilmovi.features.movies.domain.MovieRepository
import com.example.mobilnaappfilmovi.features.movies.movieIdOrThrow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val movieId: String = savedStateHandle.movieIdOrThrow

    private val _state = MutableStateFlow(
        MovieDetailsContract.UiState()
    )

    val state = _state.asStateFlow()

    private val _effects =
        MutableSharedFlow<MovieDetailsContract.SideEffect>()

    val effects = _effects.asSharedFlow()

    init {
        observeMovie()
        refreshMovie()

    }

    private fun setState(
        reducer: MovieDetailsContract.UiState.() -> MovieDetailsContract.UiState
    ) {
        _state.getAndUpdate(reducer)
    }

    fun onEvent(event: MovieDetailsContract.UiEvent) {
        when (event) {

            is MovieDetailsContract.UiEvent.LoadDetails -> {
                refreshMovie()
            }

            is MovieDetailsContract.UiEvent.BackClicked -> {
                viewModelScope.launch {
                    _effects.emit(
                        MovieDetailsContract.SideEffect.NavigateBack
                    )
                }
            }
        }
    }

    private fun observeMovie() {

        viewModelScope.launch {
            movieRepository.observeMovie(movieId)
                .catch { error ->
                    setState {
                        copy(isLoading=false,
                            error = error.message)
                    }
                }.collect { movie->
                    setState { copy(
                        movieDetails=movie,
                        isLoading=false,
                    ) }
                }


            }
        }

    private fun refreshMovie()
    {
        viewModelScope.launch {
            setState { copy(isLoading=true, error = null,) }
            runCatching {
                movieRepository.refreshMovie(movieId)
            }.onFailure { error ->

                setState {
                    copy(
                        isLoading = false,
                        error = error.message,
                    )
                } }
        }
    }

}