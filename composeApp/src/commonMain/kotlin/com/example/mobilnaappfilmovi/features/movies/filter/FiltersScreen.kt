package com.example.mobilnaappfilmovi.features.movies.filter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import projekat.features.movies.domain.Filters
import projekat.features.movies.filter.components.GenreSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreen(
    viewModel: projekat.features.movies.filter.FiltersViewModel,
    onNavigateBack: () -> Unit,
    onApplyFilters: (projekat.features.movies.domain.Filters) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is projekat.features.movies.filter.FiltersContract.SideEffect.ApplyFilters -> onApplyFilters(effect.filters)
                is projekat.features.movies.filter.FiltersContract.SideEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filter Movies", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(_root_ide_package_.projekat.features.movies.filter.FiltersContract.UiEvent.BackClicked) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F0F0F))
            )
        },
        bottomBar = {
            _root_ide_package_.projekat.features.movies.filter.FilterActionButtons(
                onClear = { viewModel.onEvent(_root_ide_package_.projekat.features.movies.filter.FiltersContract.UiEvent.ClearAllClicked) },
                onApply = { viewModel.onEvent(_root_ide_package_.projekat.features.movies.filter.FiltersContract.UiEvent.ApplyFiltersClicked) }
            )
        },
        containerColor = Color(0xFF0F0F0F)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            _root_ide_package_.projekat.features.movies.filter.FilterTextField(
                label = "Search by title",
                value = state.selectedFilters.query ?: "",
                onValueChange = {
                    viewModel.onEvent(
                        _root_ide_package_.projekat.features.movies.filter.FiltersContract.UiEvent.QueryChanged(
                            it
                        )
                    )
                }
            )

            _root_ide_package_.projekat.features.movies.filter.components.GenreSection(
                genres = state.availavleGenres,
                selectedGenreId = state.selectedFilters.genreId,
                onGenreSelected = {
                    viewModel.onEvent(
                        _root_ide_package_.projekat.features.movies.filter.FiltersContract.UiEvent.GenreSelected(
                            it
                        )
                    )
                }
            )

            _root_ide_package_.projekat.features.movies.filter.RatingSliderSection(
                rating = state.selectedFilters.minRating ?: 0f,
                onRatingChange = {
                    viewModel.onEvent(
                        _root_ide_package_.projekat.features.movies.filter.FiltersContract.UiEvent.MinRatingChanged(
                            it
                        )
                    )
                }
            )

            _root_ide_package_.projekat.features.movies.filter.YearRangeSection(
                minYear = state.selectedFilters.minYear,
                maxYear = state.selectedFilters.maxYear,
                onYearChange = { min, max ->
                    viewModel.onEvent(
                        _root_ide_package_.projekat.features.movies.filter.FiltersContract.UiEvent.YearRangeChanged(
                            min,
                            max
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun FilterTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Red,
            unfocusedBorderColor = Color.Gray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = Color.Red
        ),
        singleLine = true
    )
}

@Composable
fun RatingSliderSection(rating: Float, onRatingChange: (Float) -> Unit) {
    Column {
        Text(
            text = "Minimum Rating: ${String.format("%.1f", rating)}",
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
        Slider(
            value = rating,
            onValueChange = onRatingChange,
            valueRange = 0f..10f,
            steps = 28,
            colors = SliderDefaults.colors(
                thumbColor = Color.Red,
                activeTrackColor = Color.Red,
                inactiveTrackColor = Color.DarkGray
            )
        )
    }
}

@Composable
fun YearRangeSection(minYear: Int?, maxYear: Int?, onYearChange: (Int?, Int?) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = minYear?.toString() ?: "",
            onValueChange = { onYearChange(it.toIntOrNull(), maxYear) },
            label = { Text("Min Year") },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, focusedBorderColor = Color.Red)
        )
        OutlinedTextField(
            value = maxYear?.toString() ?: "",
            onValueChange = { onYearChange(minYear, it.toIntOrNull()) },
            label = { Text("Max Year") },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, focusedBorderColor = Color.Red)
        )
    }
}

@Composable
fun FilterActionButtons(onClear: () -> Unit, onApply: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(
            onClick = onClear,
            modifier = Modifier.weight(1f)
        ) {
            Text("Clear All", color = Color.LightGray)
        }

        Button(
            onClick = onApply,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Apply Filters", color = Color.White)
        }
    }
}