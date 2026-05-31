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
}