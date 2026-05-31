package com.example.mobilnaappfilmovi.features.movies

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.compose.viewmodel.koinViewModel

import com.example.mobilnaappfilmovi.features.movies.details.MovieDetailsScreen
import com.example.mobilnaappfilmovi.features.movies.details.MovieDetailsViewModel
import com.example.mobilnaappfilmovi.features.movies.filter.FiltersScreen
import com.example.mobilnaappfilmovi.features.movies.filter.FiltersViewModel
import com.example.mobilnaappfilmovi.features.movies.list.MoviesListContract
import com.example.mobilnaappfilmovi.features.movies.list.MoviesListScreen
import com.example.mobilnaappfilmovi.features.movies.list.MoviesListViewModel

@Composable
fun MoviesNavigation(
    startDestination: String,
) {
    val navController = rememberNavController()
    val moviesListViewModel = koinViewModel<MoviesListViewModel>()

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        composable("movies") {
            MoviesListScreen(
                viewModel = moviesListViewModel,
                onNavigateToDetails = {
                    navController.navigateToMovieDetails(it)
                },
                onNavigateToFilters = {
                    navController.navigate("filters")
                }
            )
        }

        composable(
            route = "movies/{$MOVIE_ID}",
            arguments = listOf(
                navArgument(MOVIE_ID) {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) {

            val viewModel = koinViewModel<MovieDetailsViewModel>()

            MovieDetailsScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable("filters") {

            val filtersViewModel = koinViewModel<FiltersViewModel>()

            FiltersScreen(
                viewModel = filtersViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onApplyFilters = { izabraniFilteri ->

                    moviesListViewModel.onEvent(
                        MoviesListContract.UiEvent.ApplyFilters(
                            izabraniFilteri
                        )
                    )

                    navController.popBackStack()
                }
            )
        }
    }
}

private fun NavController.navigateToMovieDetails(movieId: String) {
    navigate("movies/$movieId")
}

const val MOVIE_ID = "movieId"

val SavedStateHandle.movieId: String?
    get() = get(MOVIE_ID)

val SavedStateHandle.movieIdOrThrow: String
    get() = get(MOVIE_ID)
        ?: throw IllegalStateException("$MOVIE_ID is mandatory")