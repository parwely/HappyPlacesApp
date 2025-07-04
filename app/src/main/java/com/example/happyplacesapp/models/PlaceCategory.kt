package com.example.happyplacesapp.models

import com.example.happyplacesapp.data.database.HappyPlace
import com.example.happyplacesapp.data.database.HappyPlaceDao
import kotlinx.coroutines.flow.Flow

class HappyPlaceRepository(private val dao: HappyPlaceDao) {

    fun getAllPlaces(): Flow<List<HappyPlace>> = dao.getAllPlaces()

    suspend fun insertPlace(place: HappyPlace): Long = dao.insertPlace(place)

    suspend fun updatePlace(place: HappyPlace) = dao.updatePlace(place)

    suspend fun deletePlace(place: HappyPlace) = dao.deletePlace(place)

    suspend fun getPlaceById(id: Int): HappyPlace? = dao.getPlaceById(id)

    fun searchPlaces(query: String): Flow<List<HappyPlace>> = dao.searchPlaces(query)
}