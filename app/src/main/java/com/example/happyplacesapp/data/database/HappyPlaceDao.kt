package com.example.happyplacesapp.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HappyPlaceDao {

    @Query("SELECT * FROM happy_places_table ORDER BY dateAdded DESC")
    fun getAllHappyPlaces(): Flow<List<HappyPlace>>

    @Query("SELECT * FROM happy_places_table WHERE id = :id")
    suspend fun getHappyPlaceById(id: Long): HappyPlace?

    @Query("SELECT * FROM happy_places_table WHERE title LIKE :searchQuery OR location LIKE :searchQuery")
    suspend fun searchHappyPlaces(searchQuery: String): List<HappyPlace>

    @Query("SELECT * FROM happy_places_table WHERE category = :category")
    suspend fun getHappyPlacesByCategory(category: String): List<HappyPlace>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHappyPlace(happyPlace: HappyPlace): Long

    @Update
    suspend fun updateHappyPlace(happyPlace: HappyPlace): Int

    @Delete
    suspend fun deleteHappyPlace(happyPlace: HappyPlace): Int

    @Query("DELETE FROM happy_places_table WHERE id = :id")
    suspend fun deleteHappyPlaceById(id: Long): Int

    @Query("DELETE FROM happy_places_table")
    suspend fun deleteAllHappyPlaces(): Int
}