package com.example.mobilnaappfilmovi.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.mobilnaappfilmovi.features.movies.db.GenreEntity
import com.example.mobilnaappfilmovi.features.movies.db.MovieDetailsEntity
import com.example.mobilnaappfilmovi.features.movies.db.MovieEntity
import com.example.mobilnaappfilmovi.features.movies.db.MovieGenreCrossRef
import com.example.mobilnaappfilmovi.features.movies.db.MoviesDao
import com.example.mobilnaappfilmovi.features.profile.db.UserDao
import com.example.mobilnaappfilmovi.features.profile.db.UserEntity
import kotlinx.coroutines.Dispatchers


@Database(
    entities = [
        MovieEntity::class,
        GenreEntity::class,
        MovieGenreCrossRef::class,
        MovieDetailsEntity::class,
        UserEntity::class,
    ],
    version = 1,
    exportSchema = true
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao
    abstract fun userDao(): UserDao
}

@Suppress(
    "NO_ACTUAL_FOR_EXPECT",
    "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"
)
expect object AppDatabaseConstructor :
    RoomDatabaseConstructor<AppDatabase> {

    override fun initialize(): AppDatabase
}

fun buildAppDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {

    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigrationOnDowngrade(
            dropAllTables = true
        )
        .build()
}
