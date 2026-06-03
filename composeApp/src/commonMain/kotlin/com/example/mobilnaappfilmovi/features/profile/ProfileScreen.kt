package com.example.mobilnaappfilmovi.features.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogoutSuccess: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->

            when (effect) {

                ProfileContract.SideEffect.NavigateToLogin -> {
                    onLogoutSuccess()
                }

                is ProfileContract.SideEffect.ShowError -> {
                    // snackbar kasnije
                }

                else -> Unit
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Profile")

        Button(
            onClick = {
                viewModel.onEvent(
                    ProfileContract.UiEvent.LogoutClicked
                )
            }
        ) {

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                Text("Logout")
            }
        }
    }
}