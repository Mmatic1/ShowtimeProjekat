package com.example.mobilnaappfilmovi.features

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mobilnaappfilmovi.features.auth.landing.AuthLandingScreen
import com.example.mobilnaappfilmovi.features.auth.login.LoginScreen
import com.example.mobilnaappfilmovi.features.auth.login.LoginViewModel
import com.example.mobilnaappfilmovi.features.auth.signup.SignupScreen
import com.example.mobilnaappfilmovi.features.auth.signup.SignupViewModel
import com.example.mobilnaappfilmovi.features.movies.details.MovieDetailsScreen
import com.example.mobilnaappfilmovi.features.movies.details.MovieDetailsViewModel
import com.example.mobilnaappfilmovi.features.movies.list.MoviesListScreen
import com.example.mobilnaappfilmovi.features.movies.list.MoviesListViewModel
import com.example.mobilnaappfilmovi.features.movies.saved.FavoriteMoviesScreen
import com.example.mobilnaappfilmovi.features.movies.saved.FavoriteMoviesViewModel
import com.example.mobilnaappfilmovi.features.movies.saved.WatchlistMoviesScreen
import com.example.mobilnaappfilmovi.features.movies.saved.WatchlistMoviesViewModel
import com.example.mobilnaappfilmovi.features.profile.ProfileScreen
import com.example.mobilnaappfilmovi.features.profile.ProfileViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ShowtimeNavigation(
    startDestination: String,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        composable("authLanding") {
            AuthLandingScreen(
                onLoginClick = {
                    navController.navigate("login")
                },
                onSignupClick = {
                    navController.navigate("signup")
                }
            )
        }

        composable("login") {
            val viewModel = koinViewModel<LoginViewModel>()

            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate("movies") {
                        popUpTo("authLanding") {
                            inclusive = true
                        }
                    }
                },
                onNavigateToSignup = {
                    navController.navigate("signup")
                }
            )
        }

        composable("signup") {
            val viewModel = koinViewModel<SignupViewModel>()

            SignupScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSignupSuccess = {
                    navController.navigate("movies") {
                        popUpTo("authLanding") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("movies") {
            val viewModel = koinViewModel<MoviesListViewModel>()

            MoviesListScreen(
                viewModel = viewModel,
                onNavigateToDetails = {
                    navController.navigate("movies/$it")
                },
                onNavigateToFilters = {
                    navController.navigate("filters")
                },
                onNavigateToFavorites = {
                    navController.navigate("favorites")
                },
                onNavigateToWatchlist = {
                    navController.navigate("watchlist")
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                }
                )
        }

        composable(
            route = "movies/{$MOVIE_ID}",
            arguments = listOf(
                navArgument(MOVIE_ID) {
                    type = NavType.StringType
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

        composable("favorites") {
            val viewModel = koinViewModel<FavoriteMoviesViewModel>()

            FavoriteMoviesScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToDetails = {
                    navController.navigate("movies/$it")
                }
            )
        }

        composable("watchlist") {
            val viewModel = koinViewModel<WatchlistMoviesViewModel>()

            WatchlistMoviesScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToDetails = {
                    navController.navigate("movies/$it")
                }
            )
        }

        composable("quiz") {
            // QuizScreen
        }

        composable("profile") {

            val viewModel = koinViewModel<ProfileViewModel>()

            ProfileScreen(
                viewModel = viewModel,
                onLogoutSuccess = {
                    navController.navigate("authLanding") {
                        popUpTo(0)
                    }
                }
            )
        }
        composable("filters") {
            // FiltersScreen
        }
    }
}

const val MOVIE_ID = "movieId"

val SavedStateHandle.movieId: String?
    get() = get(MOVIE_ID)

val SavedStateHandle.movieIdOrThrow: String
    get() = get(MOVIE_ID)
        ?: throw IllegalStateException("$MOVIE_ID is mandatory")
