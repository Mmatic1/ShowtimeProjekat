package com.example.mobilnaappfilmovi.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilnaappfilmovi.core.auth.AuthStore
import com.example.mobilnaappfilmovi.core.auth.model.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authStore: AuthStore,
) : ViewModel(){
    private val _bootState= MutableStateFlow<BootState>(BootState.Loading)
    val bootState=_bootState.asStateFlow()
    private val _isLoggedIn=MutableStateFlow(false)
    val isLoggedIn=_isLoggedIn.asStateFlow()
    init{
        chechAuthState()
    }

    private fun chechAuthState()=
        viewModelScope.launch {
            try{
                val authState=authStore.awaitInitialAuthState()
                _isLoggedIn.value=authState is AuthState.Authenticated
                _bootState.value= BootState.Success
            }catch (e: Exception){
                _bootState.value= BootState.Failed(e)
            }
        }
}