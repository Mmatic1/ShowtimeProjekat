package com.example.mobilnaappfilmovi.networking.di
import com.example.mobilnaappfilmovi.core.auth.AuthStore
import com.example.mobilnaappfilmovi.core.auth.model.AuthState
import com.example.mobilnaappfilmovi.networking.AuthApi
import com.example.mobilnaappfilmovi.networking.HttpClientFactory
import com.example.mobilnaappfilmovi.networking.MoviesApi
import com.example.mobilnaappfilmovi.networking.createAuthApi
import com.example.mobilnaappfilmovi.networking.createMoviesApi
import de.jensklingenberg.ktorfit.Ktorfit
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.api.Send
import io.ktor.client.plugins.api.SetupRequest
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import org.koin.core.component.inject
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.dsl.module


val networkingModule = module {
    single<HttpClient>(Qualifiers.Unauthenticated){
        HttpClientFactory.createHttpClientWithDefaultConfig()
    }

    single<HttpClient>(Qualifiers.Authenticated){
        val authStoreLazy:Lazy<AuthStore> = inject ()
        HttpClientFactory.createHttpClientWithDefaultConfig {
            installAuthPlugin(authStoreLazy)
        }
    }
    single<MoviesApi> {
        Ktorfit.Builder()
            .httpClient(get<HttpClient>(Qualifiers.Unauthenticated))
            .baseUrl("https://rma.finlab.rs/")
            .build()
            .createMoviesApi()
    }
    single<AuthApi> {
        Ktorfit.Builder()
            .baseUrl("https://rma.finlab.rs/")
            .httpClient(
                get<HttpClient>(Qualifiers.Unauthenticated)
            )
            .build()
            .createAuthApi()
    }
}
private fun HttpClientConfig<*>.installAuthPlugin(
    authStoreLazy: Lazy<AuthStore>,
) = install(createClientPlugin("AuthPlugin") {

    on(SetupRequest) { request ->

        when (
            val authState =
                authStoreLazy.value.authState.value
        ) {

            is AuthState.Authenticated -> {
                request.header(
                    HttpHeaders.Authorization,
                    "Bearer ${authState.data.accessToken}"
                )
            }

            AuthState.Unauthenticated -> Unit
        }
    }

    on(Send) { request ->

        val call = proceed(request)

        if (
            call.response.status ==
            HttpStatusCode.Unauthorized
        ) {
            runBlocking {
                authStoreLazy.value.clearAuthData()
            }
        }

        call
    }
}
)