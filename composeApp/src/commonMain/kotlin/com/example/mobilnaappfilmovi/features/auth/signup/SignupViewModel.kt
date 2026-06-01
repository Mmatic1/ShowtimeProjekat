package com.example.mobilnaappfilmovi.features.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilnaappfilmovi.features.auth.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch

class SignupViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SignupContract.State())
    val state = _state.asStateFlow()

    private fun setState(
        reducer: SignupContract.State.() -> SignupContract.State,
    ) {
        _state.getAndUpdate(reducer)
    }

    private val events = MutableSharedFlow<SignupContract.UiEvent>()

    private val _effects = MutableSharedFlow<SignupContract.Effect>()
    val effects = _effects.asSharedFlow()

    fun setEvent(event: SignupContract.UiEvent) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    init {
        observeEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->

                when (event) {

                    is SignupContract.UiEvent.FullNameChanged ->
                        setState {
                            copy(
                                fullName = event.value,
                                error = null,
                            )
                        }

                    is SignupContract.UiEvent.UsernameChanged ->
                        setState {
                            copy(
                                username = event.value,
                                error = null,
                            )
                        }

                    is SignupContract.UiEvent.PasswordChanged ->
                        setState {
                            copy(
                                password = event.value,
                                error = null,
                            )
                        }

                    SignupContract.UiEvent.SignupClicked ->
                        signup()
                }
            }
        }
    }

    private fun signup() {

        if (state.value.isLoading) return

        viewModelScope.launch {

            if (state.value.fullName.isBlank()) {
                setState {
                    copy(error = "Full name is required")
                }
                return@launch
            }

            if (state.value.username.isBlank()) {
                setState {
                    copy(error = "Username is required")
                }
                return@launch
            }

            if (state.value.password.length < 8) {
                setState {
                    copy(error = "Password must contain at least 8 characters")
                }
                return@launch
            }

            setState {
                copy(
                    isLoading = true,
                    error = null,
                )
            }

            runCatching {

                authRepository.signup(
                    fullName = state.value.fullName,
                    username = state.value.username,
                    password = state.value.password,
                )

            }.onSuccess {

                setState {
                    copy(isLoading = false)
                }

                _effects.emit(
                    SignupContract.Effect.NavigateToMovies
                )

            }.onFailure {

                setState {
                    copy(
                        isLoading = false,
                        error = it.message ?: "Signup failed",
                    )
                }
            }
        }
    }
}