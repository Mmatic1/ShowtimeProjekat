package com.example.mobilnaappfilmovi.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilnaappfilmovi.features.auth.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
): ViewModel(){
    private val _state= MutableStateFlow(LoginContract.State())
    val state=_state.asStateFlow()
    private fun setState(reducer: LoginContract.State.()-> LoginContract.State,)
    {
        _state.getAndUpdate(reducer)
    }

    private val _effects = MutableSharedFlow<LoginContract.Effect>()

    val effects = _effects.asSharedFlow()
    private val events= MutableSharedFlow<LoginContract.UiEvent>()

    fun setEvent(event: LoginContract.UiEvent, ) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    init{
        observeEvents()
    }

    private fun observeEvents(){
        viewModelScope.launch {
            events.collect { event->
                when(event){
                    is LoginContract.UiEvent.UsernameChanged -> setState {
                        copy(username=event.value)
                    }

                    is LoginContract.UiEvent.PasswordChanged -> setState {
                        copy(password=event.value)
                    }

                    LoginContract.UiEvent.LoginClicked-> login()

                }
            }
        }
    }


    private fun login() {
        viewModelScope.launch {

            setState {
                copy(
                    isLoading = true,
                    error = null,
                )
            }

            runCatching {
                authRepository.login(
                    username = state.value.username,
                    password = state.value.password,
                )
            }
                .onSuccess {

                    setState {
                        copy(isLoading = false)
                    }

                    _effects.emit(
                        LoginContract.Effect.NavigateToMovies
                    )
                }
                .onFailure {

                    setState {
                        copy(
                            error = it.message ?: "Login failed"
                        )
                    }
                }

            setState {
                copy(
                    isLoading = false
                )
            }
        }
    }
}