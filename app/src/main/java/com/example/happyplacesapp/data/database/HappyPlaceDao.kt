package com.example.happyplacesapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HappyPlaceDao {

    @Query("SELECT * FROM happy_places_table ORDER BY dateAdded DESC")
    fun getAllHappyPlaces(): LiveData<List<HappyPlace>>

    @Query("SELECT * FROM happy_places_table WHERE id = :id")
    fun getHappyPlaceById(id: Long): HappyPlace?

    @Query("SELECT * FROM happy_places_table WHERE title LIKE :searchQuery OR location LIKE :searchQuery")
    fun searchHappyPlaces(searchQuery: String): List<HappyPlace>

    @Query("SELECT * FROM happy_places_table WHERE category = :category")
    fun getHappyPlacesByCategory(category: String): List<HappyPlace>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(happyPlace: HappyPlace): Long

    @Update
    fun update(happyPlace: HappyPlace): Int

    @Delete
    fun delete(happyPlace: HappyPlace): Int

    @Query("DELETE FROM happy_places_table WHERE id = :id")
    fun deleteHappyPlaceById(id: Long): Int

    @Query("DELETE FROM happy_places_table")
    fun deleteAllHappyPlaces(): Int
}