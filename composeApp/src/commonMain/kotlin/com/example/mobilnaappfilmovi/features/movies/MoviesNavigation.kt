package com.example.mobilnaappfilmovi.features.movies

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mobilnaappfilmovi.features.movies.list.MoviesListViewModel
import org.koin.compose.viewmodel.koinViewModel
import projekat.features.movies.details.MovieDetailsScreen
import projekat.features.movies.details.MovieDetailsViewModel
import projekat.features.movies.filter.FiltersScreen
import projekat.features.movies.filter.FiltersViewModel
import projekat.features.movies.list.MoviesListContract
import projekat.features.movies.list.MoviesListScreen
import projekat.features.movies.list.MoviesListViewModel
import projekat.features.movies.navigateToMovieDetails

@Composable
fun MoviesNavigation(
    startDestination: String,
){
    val navController = rememberNavController()
    val moviesListViewModel=koinViewModel<MoviesListViewModel>()
    NavHost(
        navController=navController,
        startDestination=startDestination,
    ) {
        composable(
            route="movies",
        )
        {
            _root_ide_package_.projekat.features.movies.list.MoviesListScreen(
                viewModel = moviesListViewModel,
                onNavigateToDetails = {
                    navController.navigateToMovieDetails(movieId = it)
                },
                onNavigateToFilters = {
                    navController.navigate("filters")
                }
            )
        }
        composable (
            route="movies/{${_root_ide_package_.projekat.features.movies.MOVIE_ID}}",
            arguments = listOf(
                navArgument(_root_ide_package_.projekat.features.movies.MOVIE_ID){
                    type= NavType.StringType
                    nullable=false
                }
            ),
        )
        {
            val viewModel = koinViewModel<projekat.features.movies.details.MovieDetailsViewModel>()

            _root_ide_package_.projekat.features.movies.details.MovieDetailsScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.navigateUp()
                })
        }
        composable("filters") {
            val filtersViewModel = koinViewModel<projekat.features.movies.filter.FiltersViewModel>()

            _root_ide_package_.projekat.features.movies.filter.FiltersScreen(
                viewModel = filtersViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onApplyFilters = { izabraniFilterii ->
                    moviesListViewModel.onEvent(
                        _root_ide_package_.projekat.features.movies.list.MoviesListContract.UiEvent.ApplyFilters(
                            izabraniFilterii
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

inline val SavedStateHandle.movieId: String?
    get() = get(_root_ide_package_.projekat.features.movies.MOVIE_ID)

inline val SavedStateHandle.movieIdOrThrow: String
    get() = get(_root_ide_package_.projekat.features.movies.MOVIE_ID)
        ?: throw IllegalStateException("${_root_ide_package_.projekat.features.movies.MOVIE_ID} is mandatory")