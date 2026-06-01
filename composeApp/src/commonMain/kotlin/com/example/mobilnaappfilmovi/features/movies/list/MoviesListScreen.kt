package com.example.mobilnaappfilmovi.features.movies.list

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobilnaappfilmovi.features.movies.domain.Movie
import com.example.mobilnaappfilmovi.features.movies.domain.SortType
import com.example.mobilnaappfilmovi.features.movies.list.components.MovieItem
import com.example.mobilnaappfilmovi.features.movies.list.components.MoviesTopBar

@Composable
fun MoviesListScreen(
    viewModel: MoviesListViewModel,
    onNavigateToDetails: (String) -> Unit,
    onNavigateToFilters: () -> Unit,

) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    val activeFiltersCount = remember(state.filters) {

        var count = 0

        if (!state.filters.query.isNullOrBlank()) {
            count++
        }

        if (state.filters.genreId != null) {
            count++
        }

        if ((state.filters.minRating ?: 0f) > 0f) {
            count++
        }

        if (
            state.filters.minYear != null ||
            state.filters.maxYear != null
        ) {
            count++
        }

        count
    }

    LaunchedEffect(Unit) {

        viewModel.effects.collect { effect ->

            when (effect) {

                is MoviesListContract.SideEffect.NavigateToDetails -> {
                    onNavigateToDetails(effect.movieId)
                }

                is MoviesListContract.SideEffect.NavigateToFilters -> {
                    onNavigateToFilters()
                }

                is MoviesListContract.SideEffect.ShowError -> Unit


            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F0F))
    ) {

        MoviesTopBar(
            filtersCount = activeFiltersCount,
            onFilterClick = {
                viewModel.onEvent(
                    MoviesListContract.UiEvent.FilterClicked
                )
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            ) {



               Button(
                    onClick = {
                        viewModel.onEvent(
                            MoviesListContract.UiEvent.SortClicked
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE50914)
                            .copy(alpha = 0.2f),
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {

                    Text(
                        text =
                            "Sort by: ${
                                state.sort.name
                                    .lowercase()
                                    .replaceFirstChar {
                                        it.uppercase()
                                    }
                            }"
                    )
                }

                DropdownMenu(
                    expanded = state.showSortMenu,
                    onDismissRequest = {
                        viewModel.onEvent(
                            MoviesListContract.UiEvent.SortDismissed
                        )
                    },
                    modifier = Modifier.background(
                        Color(0xFF1E1E1E)
                    )
                ) {

                    SortType.entries.forEach { type ->

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = type.name,
                                    color = Color.White
                                )
                            },
                            onClick = {
                                viewModel.onEvent(
                                    MoviesListContract.UiEvent.ChangeSort(
                                        type
                                    )
                                )
                            }
                        )
                    }
                }
            }

            Text(
                text = "${state.totalMovies} movies",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray
            )
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            when {

                state.isLoading &&
                        state.movies.isEmpty() -> {

                    LoadingView()
                }

                state.error != null &&
                        state.movies.isEmpty() -> {

                    ErrorView(
                        message = state.error,
                        onRetry = {
                            viewModel.onEvent(
                                MoviesListContract.UiEvent.Retry
                            )
                        }
                    )
                }

                state.movies.isEmpty() -> {

                    EmptyView()
                }

                else -> {

                    Box {

                        MoviesList(
                            movies = state.movies,
                            onMovieClick = { movieId ->

                                viewModel.onEvent(
                                    MoviesListContract.UiEvent.MovieClicked(
                                        movieId
                                    )
                                )
                            }
                        )

                        if (state.isRefreshing) {

                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.TopCenter)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MoviesList(
    movies: List<Movie>,
    onMovieClick: (String) -> Unit,
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(
            items = movies,
            key = { movie -> movie.id }
        ) { movie ->

            MovieItem(
                movie = movie,
                onClick = {
                    onMovieClick(movie.id)
                }
            )
        }
    }
}

@Composable
fun LoadingView() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        CircularProgressIndicator(
            color = Color.Red
        )
    }
}

@Composable
fun ErrorView(
    message: String?,
    onRetry: () -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = message ?: "Something went wrong",
            color = Color.White
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            )
        ) {

            Text("Retry")
        }
    }
}

@Composable
fun EmptyView() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "No movies found",
            color = Color.Gray
        )
    }
}