package com.example.mobilnaappfilmovi.features.movies.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilnaappfilmovi.features.movies.domain.MovieRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch

class FavoriteMoviesViewModel(
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SavedMoviesContract.UiState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<SavedMoviesContract.SideEffect>()
    val effects = _effects.asSharedFlow()

    init {
        observeFavorites()
        refreshFavorites()
    }

    fun onEvent(event: SavedMoviesContract.UiEvent) {
        when (event) {
            SavedMoviesContract.UiEvent.Load,
            SavedMoviesContract.UiEvent.Retry -> refreshFavorites()

            SavedMoviesContract.UiEvent.BackClicked -> emitEffect(
                SavedMoviesContract.SideEffect.NavigateBack
            )

            SavedMoviesContract.UiEvent.SyncMessageShown -> setState {
                copy(syncMessage = null)
            }

            is SavedMoviesContract.UiEvent.MovieClicked -> emitEffect(
                SavedMoviesContract.SideEffect.NavigateToDetails(event.movieId)
            )

            is SavedMoviesContract.UiEvent.RemoveClicked -> removeFavorite(event.movieId)
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            movieRepository.observeFavorites().collectLatest { movies ->
                setState {
                    copy(
                        movies = movies,
                        isLoading = false,
                        error = null,
                    )
                }
            }
        }
    }

    private fun refreshFavorites() {
        viewModelScope.launch {
            val hasMovies = state.value.movies.isNotEmpty()
            setState {
                copy(
                    isLoading = !hasMovies,
                    isRefreshing = hasMovies,
                    error = null,
                )
            }

            runCatching {
                movieRepository.refreshFavorites()
            }.onSuccess {
                setState { copy(isLoading = false, isRefreshing = false) }
            }.onFailure { error ->
                setState {
                    copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = error.message ?: "Could not sync favorites",
                    )
                }
            }
        }
    }

    private fun removeFavorite(movieId: String) {
        viewModelScope.launch {
            movieRepository.updateFavorite(movieId, false)
            runCatching {
                movieRepository.removeFavorite(movieId)
            }.onFailure {
                movieRepository.updateFavorite(movieId, true)
                setState {
                    copy(syncMessage = "Favorite removal failed. Change was reverted.")
                }
            }
        }
    }

    private fun setState(
        reducer: SavedMoviesContract.UiState.() -> SavedMoviesContract.UiState,
    ) {
        _state.getAndUpdate(reducer)
    }

    private fun emitEffect(effect: SavedMoviesContract.SideEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
