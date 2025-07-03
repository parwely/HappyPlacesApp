package com.example.happyplacesapp.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HappyPlaceDao {

    @Query("SELECT * FROM happy_places ORDER BY dateAdded DESC")
    fun getAllPlaces(): Flow<List<HappyPlace>>

    @Query("SELECT * FROM happy_places WHERE id = :id")
    suspend fun getPlaceById(id: Int): HappyPlace?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place: HappyPlace): Long

    @Update
    suspend fun updatePlace(place: HappyPlace): Int

    @Delete
    suspend fun deletePlace(place: HappyPlace): Int

    @Query("SELECT * FROM happy_places WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchPlaces(query: String): Flow<List<HappyPlace>>
}