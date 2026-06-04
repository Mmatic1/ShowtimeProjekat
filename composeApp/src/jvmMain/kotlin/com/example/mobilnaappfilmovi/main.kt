package com.example.mobilnaappfilmovi

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.mobilnaappfilmovi.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "MobilnaAppFIlmovi",
    ) {
        AppM()
    }
}