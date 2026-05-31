package com.example.mobilnaappfilmovi.networking

import kotlinx.serialization.json.Json

val NetworkingJson = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}