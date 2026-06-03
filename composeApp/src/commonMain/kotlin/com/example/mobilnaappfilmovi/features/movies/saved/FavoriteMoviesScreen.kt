package com.example.mobilnaappfilmovi.features.movies.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobilnaappfilmovi.features.movies.domain.Movie
import com.example.mobilnaappfilmovi.features.movies.list.components.MovieItem
import kotlinx.coroutines.delay

@Composable
fun FavoriteMoviesScreen(
    viewModel: FavoriteMoviesViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                SavedMoviesContract.SideEffect.NavigateBack -> onNavigateBack()
                is SavedMoviesContract.SideEffect.NavigateToDetails -> {
                    onNavigateToDetails(effect.movieId)
                }
            }
        }
    }

    SavedMoviesScreen(
        title = "Favorite",
        emptyText = "No favorite movies yet",
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        onNavigateToDetails = onNavigateToDetails,
    )
}

@Composable
fun WatchlistMoviesScreen(
    viewModel: WatchlistMoviesViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                SavedMoviesContract.SideEffect.NavigateBack -> onNavigateBack()
                is SavedMoviesContract.SideEffect.NavigateToDetails -> {
                    onNavigateToDetails(effect.movieId)
                }
            }
        }
    }

    SavedMoviesScreen(
        title = "Watchlist",
        emptyText = "No movies in watchlist yet",
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        onNavigateToDetails = onNavigateToDetails,
    )
}

@Composable
private fun SavedMoviesScreen(
    title: String,
    emptyText: String,
    state: SavedMoviesContract.UiState,
    onEvent: (SavedMoviesContract.UiEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
) {
    LaunchedEffect(Unit) {
        onEvent(SavedMoviesContract.UiEvent.Load)
    }

    LaunchedEffect(state.syncMessage) {
        if (state.syncMessage != null) {
            delay(3000)
            onEvent(SavedMoviesContract.UiEvent.SyncMessageShown)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F0F))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { onEvent(SavedMoviesContract.UiEvent.BackClicked) }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                )
            }

            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
            )
        }

        if (state.isRefreshing) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        if (state.syncMessage != null) {
            Text(
                text = state.syncMessage,
                color = Color(0xFFFFA0A0),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodySmall,
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading && state.movies.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red,
                    )
                }

                state.error != null && state.movies.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = state.error,
                            color = Color.White,
                        )
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = { onEvent(SavedMoviesContract.UiEvent.Retry) }) {
                            Text("Retry")
                        }
                    }
                }

                state.movies.isEmpty() -> {
                    Text(
                        text = emptyText,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                else -> {
                    SavedMoviesList(
                        movies = state.movies,
                        onMovieClick = { movieId ->
                            onEvent(SavedMoviesContract.UiEvent.MovieClicked(movieId))
                        },
                        onRemoveClick = { movieId ->
                            onEvent(SavedMoviesContract.UiEvent.RemoveClicked(movieId))
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedMoviesList(
    movies: List<Movie>,
    onMovieClick: (String) -> Unit,
    onRemoveClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            items = movies,
            key = { movie -> movie.id },
        ) { movie ->
            Column {
                MovieItem(
                    movie = movie,
                    onClick = { onMovieClick(movie.id) },
                )
                OutlinedButton(
                    onClick = { onRemoveClick(movie.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                ) {
                    Text("Remove")
                }
            }
        }
    }
}
