package com.example.mobilnaappfilmovi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.koin.android.ext.koin.androidContext
import projekat.di.initKoin
import projekat.features.movies.MoviesApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            projekat.features.movies.MoviesApp()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    projekat.features.movies.MoviesApp()
}