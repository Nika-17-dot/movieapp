package com.example.movieapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete



@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: FavoriteMovie)

    @Query("SELECT * FROM favorite_movies")
    suspend fun getAllFavorites(): List<FavoriteMovie>

    @Delete
    suspend fun deleteFavorite(movie: FavoriteMovie)
}