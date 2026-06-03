package com.example.mobilnaappfilmovi.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilnaappfilmovi.features.auth.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state =
        MutableStateFlow(ProfileContract.UiState())

    val state =
        _state.asStateFlow()

    private val _sideEffects =
        Channel<ProfileContract.SideEffect>()

    val sideEffects =
        _sideEffects.receiveAsFlow()

    fun onEvent(
        event: ProfileContract.UiEvent
    ) {
        when (event) {

            ProfileContract.UiEvent.LogoutClicked -> {
                logout()
            }

            else -> Unit
        }
    }

    private fun logout() {

        viewModelScope.launch {

            try {

                authRepository.logout()

                _sideEffects.send(
                    ProfileContract.SideEffect.NavigateToLogin
                )

            } catch (e: Exception) {

                _sideEffects.send(
                    ProfileContract.SideEffect.ShowError(
                        e.message ?: "Logout failed"
                    )
                )
            }
        }
    }
}