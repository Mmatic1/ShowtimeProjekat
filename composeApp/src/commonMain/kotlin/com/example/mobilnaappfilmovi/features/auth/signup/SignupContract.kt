package com.example.mobilnaappfilmovi.features.auth.signup

interface SignupContract {

    data class State(
        val fullName: String = "",
        val username: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed interface UiEvent {

        data class FullNameChanged(val value: String, ) : UiEvent

        data class UsernameChanged(val value: String, ) : UiEvent

        data class PasswordChanged(val value: String, ) : UiEvent

        data object SignupClicked : UiEvent
    }

    sealed interface Effect {
        data object NavigateToMovies : Effect
    }
}