package com.example.mobilnaappfilmovi.core.database

import androidx.room.RoomDatabaseConstructor

@Suppress(names = ["NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual object AppDatabaseConstructor :
    RoomDatabaseConstructor<AppDatabase> {
    actual override fun initialize(): AppDatabase {
        TODO("Not yet implemented")
    }
}