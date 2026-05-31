package com.example.mobilnaappfilmovi.features.auth.login

interface LoginContract {
    data class State(
            val username: String="",
            val password: String="",
            val isLoading: Boolean=false,
            val error: String?=null,
            )
    sealed interface UiEvent{
        data class UsernameChanged(val value: String, ): UiEvent
        data class PasswordChanged(val value: String,): UiEvent
        data object LoginClicked: UiEvent
    }
}