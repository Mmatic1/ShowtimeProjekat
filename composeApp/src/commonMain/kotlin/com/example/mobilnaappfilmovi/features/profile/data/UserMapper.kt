package com.example.mobilnaappfilmovi.features.profile.data

import com.example.mobilnaappfilmovi.features.profile.db.UserEntity
import com.example.mobilnaappfilmovi.features.profile.domain.User
import com.example.mobilnaappfilmovi.networking.model.authApiModel.UserApiModel

fun UserApiModel.toEntity()=
    UserEntity(
        id=id,
        username=username,
        fullName=fullName,
    )

fun UserEntity.toDomain()=
    User(
        id = id,
        username = username,
        fullName = fullName,
    )
fun UserApiModel.toDomain() =
    User(
        id = id,
        username = username,
        fullName = fullName,
    )