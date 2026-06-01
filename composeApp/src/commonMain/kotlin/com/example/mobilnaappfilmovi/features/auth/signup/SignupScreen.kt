package com.example.mobilnaappfilmovi.features.auth.signup


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignupScreen(
    viewModel: SignupViewModel,
    onNavigateBack: () -> Unit,
    onSignupSuccess: () -> Unit,
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                SignupContract.Effect.NavigateToMovies ->
                    onSignupSuccess()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                text = "Create account",
                style = MaterialTheme.typography.headlineMedium,
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.fullName,
                onValueChange = {
                    viewModel.setEvent(
                        SignupContract.UiEvent.FullNameChanged(it)
                    )
                },
                label = {
                    Text("Full name")
                },
                enabled = !state.isLoading,
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.username,
                onValueChange = {
                    viewModel.setEvent(
                        SignupContract.UiEvent.UsernameChanged(it)
                    )
                },
                label = {
                    Text("Username")
                },
                enabled = !state.isLoading,
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.password,
                onValueChange = {
                    viewModel.setEvent(
                        SignupContract.UiEvent.PasswordChanged(it)
                    )
                },
                label = {
                    Text("Password")
                },
                visualTransformation = PasswordVisualTransformation(),
                enabled = !state.isLoading,
            )

            state.error?.let { error ->

                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading,
                onClick = {
                    viewModel.setEvent(
                        SignupContract.UiEvent.SignupClicked
                    )
                },
            ) {
                Text("Sign up")
            }

            TextButton(
                enabled = !state.isLoading,
                onClick = onNavigateBack,
            ) {
                Text("Already have an account? Login")
            }

            if (state.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}