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

class WatchlistMoviesViewModel(
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SavedMoviesContract.UiState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<SavedMoviesContract.SideEffect>()
    val effects = _effects.asSharedFlow()

    init {
        observeWatchlist()
        refreshWatchlist()
    }

    fun onEvent(event: SavedMoviesContract.UiEvent) {
        when (event) {
            SavedMoviesContract.UiEvent.Load,
            SavedMoviesContract.UiEvent.Retry -> refreshWatchlist()

            SavedMoviesContract.UiEvent.BackClicked -> emitEffect(
                SavedMoviesContract.SideEffect.NavigateBack
            )

            SavedMoviesContract.UiEvent.SyncMessageShown -> setState {
                copy(syncMessage = null)
            }

            is SavedMoviesContract.UiEvent.MovieClicked -> emitEffect(
                SavedMoviesContract.SideEffect.NavigateToDetails(event.movieId)
            )

            is SavedMoviesContract.UiEvent.RemoveClicked -> removeFromWatchlist(event.movieId)
        }
    }

    private fun observeWatchlist() {
        viewModelScope.launch {
            movieRepository.observeWatchlist().collectLatest { movies ->
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

    private fun refreshWatchlist() {
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
                movieRepository.refreshWatchlist()
            }.onSuccess {
                setState { copy(isLoading = false, isRefreshing = false) }
            }.onFailure { error ->
                setState {
                    copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = error.message ?: "Could not sync watchlist",
                    )
                }
            }
        }
    }

    private fun removeFromWatchlist(movieId: String) {
        viewModelScope.launch {
            movieRepository.updateWatchlist(movieId, false)
            runCatching {
                movieRepository.removeFromWatchlist(movieId)
            }.onFailure {
                movieRepository.updateWatchlist(movieId, true)
                setState {
                    copy(syncMessage = "Watchlist removal failed. Change was reverted.")
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
