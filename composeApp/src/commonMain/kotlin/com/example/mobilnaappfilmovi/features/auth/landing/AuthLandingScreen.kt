package com.example.mobilnaappfilmovi.features.auth.landing


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AuthLandingScreen(
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Text(
            text = "Showtime",
            style = MaterialTheme.typography.headlineLarge,
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onLoginClick,
        ) {
            Text("Login")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSignupClick,
        ) {
            Text("Create Account")
        }
    }
}