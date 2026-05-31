package com.example.mobilnaappfilmovi.core.auth

import androidx.datastore.core.DataStore
import com.example.mobilnaappfilmovi.core.auth.model.AuthData
import com.example.mobilnaappfilmovi.core.auth.model.AuthState
import com.example.mobilnaappfilmovi.core.auth.model.asAuthenticationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

class AuthStore(
    private val persistence: DataStore<AuthData>,
) {
    private val scope= CoroutineScope(Dispatchers.IO)
    private val authData=persistence.data
        .stateIn(
            scope=scope,
            started = SharingStarted.Eagerly,
            initialValue = runBlocking { persistence.data.first()},
        )
    val authState: StateFlow<AuthState> = persistence.data
        .map { it.asAuthenticationState() }
        .distinctUntilChanged()
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = AuthState.Unauthenticated
        )

    suspend fun awaitInitialAuthState(): AuthState {
        return persistence.data
            .map { it.asAuthenticationState() }
            .first()
    }

    suspend fun setAuthData(authData: AuthData) =
        persistence.updateData {
            authData
        }

    suspend fun setAccessToken(accessToken: String) =
        persistence.updateData {
            it.copy(accessToken = accessToken)
        }

    suspend fun clearAuthData() =
        persistence.updateData { AuthData.empty() }



}