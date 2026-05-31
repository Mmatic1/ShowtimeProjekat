package com.example.mobilnaappfilmovi.features.profile.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val id:Long,
    val username: String,
    val fullName: String,
)
