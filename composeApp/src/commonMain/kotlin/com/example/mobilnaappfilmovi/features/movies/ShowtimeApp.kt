package com.example.mobilnaappfilmovi.features.movies

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.mobilnaappfilmovi.features.splash.BootState
import com.example.mobilnaappfilmovi.features.splash.SplashViewModel
import org.koin.compose.viewmodel.koinViewModel
import java.awt.SplashScreen

@Composable
fun ShowtimeApp(){
    val splashViewModel: SplashViewModel=koinViewModel()
    val bootState by splashViewModel.bootState.collectAsState()
    val isLoggedIn by splashViewModel.isLoggedIn.collectAsState()

    when(bootState){
        BootState.Loading->{
            SplashScreen()
        }
        BootState.Success -> {
            ShowtimeNavigation(
                startDestination=
                    if(isLoggedIn)
                "movies"
                else
                "authLanding"
            )
        }
        is BootState.Failed -> {
            Text("Failed to start app")
        }
    }
}