package com.example.mobilnaappfilmovi.features.profile.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user LIMIT 1")
    fun observeUser(): Flow<UserEntity>

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insertUser(
        user: UserEntity,
    )
    @Query("DELETE FROM user")
    suspend fun clear()

    @Query("SELECT * FROM favorites")
    fun observeFavorites():Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM watchlist")
    fun observeWatchlist(): Flow<List<WatchlistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(entity: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE movieId = :id")
    suspend fun removeFavorite(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchlist(entity: WatchlistEntity)

    @Query("DELETE FROM watchlist WHERE movieId = :id")
    suspend fun removeFromWatchlist(id: String)

}