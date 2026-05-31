package com.example.mobilnaappfilmovi.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuider(
    context: Context
): RoomDatabase.Builder<AppDatabase>{
    val appContext=context.applicationContext
    val dbFile=appContext.getDatabasePath(
        "showtime.db"
    )
    return Room.databaseBuilder<AppDatabase>(
        context=appContext,
        name=dbFile.absolutePath
    )
}