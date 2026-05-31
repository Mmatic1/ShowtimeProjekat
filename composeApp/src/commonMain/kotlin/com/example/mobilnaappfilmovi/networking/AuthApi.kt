package com.example.mobilnaappfilmovi.networking

import com.example.mobilnaappfilmovi.networking.model.authApiModel.AuthResponseApiModel
import com.example.mobilnaappfilmovi.networking.model.authApiModel.LoginApiModel
import com.example.mobilnaappfilmovi.networking.model.authApiModel.SignupApiModel
import com.example.mobilnaappfilmovi.networking.model.authApiModel.UserApiModel
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(
        @Body body: LoginApiModel
    ): AuthResponseApiModel

    @POST("auth/signup")
    suspend fun signup(
        @Body body: SignupApiModel
    ): AuthResponseApiModel

}