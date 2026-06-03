package com.example.mobilnaappfilmovi.features.profile

interface ProfileContract {

    data class UiState(

        val fullName: String = "",
        val username: String = "",

        val favoriteCount: Int = 0,
        val watchlistCount: Int = 0,

        val bestScore: Double = 0.0,
        val playedQuizzes: Int = 0,

        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,

        val error: String? = null
    )

    sealed interface UiEvent {

        data object LoadProfile : UiEvent

        data object Refresh : UiEvent

        data object LogoutClicked : UiEvent

        data object FavoritesClicked : UiEvent

        data object WatchlistClicked : UiEvent
    }

    sealed interface SideEffect {

        data object NavigateToLogin : SideEffect

        data object NavigateToFavorites : SideEffect

        data object NavigateToWatchlist : SideEffect

        data class ShowError(
            val message: String
        ) : SideEffect
    }
}