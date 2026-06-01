package com.example.mobilnaappfilmovi.features.profile.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val movieId: String
)
