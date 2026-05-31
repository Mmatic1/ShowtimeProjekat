package com.example.mobilnaappfilmovi

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform