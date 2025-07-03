package com.example.happyplacesapp.models

import `HappyPlace.kt`
import HappyPlaceDao
import kotlinx.coroutines.flow.Flow

class HappyPlaceRepository(private val dao: HappyPlaceDao) {

    fun getAllPlaces(): Flow<List<`HappyPlace.kt`>> = dao.getAllPlaces()

    suspend fun insertPlace(place: `HappyPlace.kt`): Long = dao.insertPlace(place)

    suspend fun updatePlace(place: `HappyPlace.kt`) = dao.updatePlace(place)

    suspend fun deletePlace(place: `HappyPlace.kt`) = dao.deletePlace(place)

    suspend fun getPlaceById(id: Int): `HappyPlace.kt`? = dao.getPlaceById(id)

    fun searchPlaces(query: String): Flow<List<`HappyPlace.kt`>> = dao.searchPlaces(query)
}