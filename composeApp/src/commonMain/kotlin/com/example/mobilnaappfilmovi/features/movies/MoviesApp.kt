package com.example.mobilnaappfilmovi.features.movies

import androidx.compose.runtime.Composable

@Composable
fun MoviesApp()
{
    MoviesNavigation(
        startDestination = "movies"
    )
}