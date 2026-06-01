package com.example.mobilnaappfilmovi

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobilnaappfilmovi.features.movies.ShowtimeApp

@Composable
fun AppM()
{
    MaterialTheme{
        ShowtimeApp()
    }
}
@Preview
@Composable
private fun AppPreview(){
    AppM()
}