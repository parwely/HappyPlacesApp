package com.example.happyplacesapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.happyplacesapp.data.database.HappyPlace
import com.example.happyplacesapp.data.database.HappyPlaceDao

class HappyPlaceRepository(private val happyPlaceDao: HappyPlaceDao) {

    val allPlaces: LiveData<List<HappyPlace>> = happyPlaceDao.getAllHappyPlaces().asLiveData()

    fun getAllHappyPlaces(): LiveData<List<HappyPlace>> {
        return happyPlaceDao.getAllHappyPlaces().asLiveData()
    }

    suspend fun getHappyPlaceById(id: Long): HappyPlace? {
        return happyPlaceDao.getHappyPlaceById(id)
    }

    suspend fun insert(place: HappyPlace) {
        happyPlaceDao.insert(place)
    }

    suspend fun update(place: HappyPlace) {
        happyPlaceDao.update(place)
    }

    suspend fun delete(place: HappyPlace) {
        happyPlaceDao.delete(place)
    }

    suspend fun deleteHappyPlaceById(id: Long): Int {
        return happyPlaceDao.deleteHappyPlaceById(id)
    }

    suspend fun searchHappyPlaces(searchQuery: String): List<HappyPlace> {
        return happyPlaceDao.searchHappyPlaces("%$searchQuery%")
    }

    suspend fun getHappyPlacesByCategory(category: String): List<HappyPlace> {
        return happyPlaceDao.getHappyPlacesByCategory(category)
    }
}