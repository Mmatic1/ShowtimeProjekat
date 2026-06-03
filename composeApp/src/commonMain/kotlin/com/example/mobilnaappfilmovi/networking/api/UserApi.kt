package com.example.mobilnaappfilmovi.networking.api

import com.example.mobilnaappfilmovi.networking.model.authApiModel.UserApiModel
import de.jensklingenberg.ktorfit.http.GET

interface UserApi {
    @GET("me")
    suspend fun me(): UserApiModel

}