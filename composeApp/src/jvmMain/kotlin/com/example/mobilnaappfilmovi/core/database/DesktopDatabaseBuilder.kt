package com.example.mobilnaappfilmovi.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>{
    val dbFile=File(
        System.getProperty("java.io.tmpdir"),
        "showtime.db"
    )
    return Room.databaseBuilder<AppDatabase>(
        name=dbFile.absolutePath
    )
}