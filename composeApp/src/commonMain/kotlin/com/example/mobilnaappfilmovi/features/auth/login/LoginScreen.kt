package com.example.mobilnaappfilmovi.features.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToSignup: () -> Unit,
    onLoginSuccess:() -> Unit,
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                LoginContract.Effect.NavigateToMovies -> {
                    onLoginSuccess()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.username,
            onValueChange = {
                viewModel.setEvent(
                    LoginContract.UiEvent.UsernameChanged(it)
                )
            },
            label = {
                Text("Username")
            },
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.password,
            onValueChange = {
                viewModel.setEvent(
                    LoginContract.UiEvent.PasswordChanged(it)
                )
            },
            label = {
                Text("Password")
            },
            visualTransformation =
                PasswordVisualTransformation(),
        )

        if (state.error != null) {
            Text(
                text = state.error!!,
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            onClick = {
                viewModel.setEvent(
                    LoginContract.UiEvent.LoginClicked
                )
            },
        ) {
            Text("Login")
        }

        TextButton(
            onClick = onNavigateToSignup,
        ) {
            Text("Create account")
        }

        if (state.isLoading) {
            CircularProgressIndicator()
        }
    }
}