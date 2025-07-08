package com.example.happyplacesapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.happyplacesapp.data.database.HappyPlace
import com.example.happyplacesapp.data.database.HappyPlaceDao
import kotlinx.coroutines.Dispatchers

class HappyPlaceRepository(private val happyPlaceDao: HappyPlaceDao) {

    fun getAllHappyPlaces(): LiveData<List<HappyPlace>> {
        return happyPlaceDao.getAllHappyPlaces().asLiveData()
    }

    suspend fun getHappyPlaceById(id: Long): HappyPlace? {
        return happyPlaceDao.getHappyPlaceById(id)
    }

    suspend fun insertHappyPlace(happyPlace: HappyPlace): Long {
        return happyPlaceDao.insertHappyPlace(happyPlace)
    }

    suspend fun updateHappyPlace(happyPlace: HappyPlace): Int {
        return happyPlaceDao.updateHappyPlace(happyPlace)
    }

    suspend fun deleteHappyPlace(happyPlace: HappyPlace): Int {
        return happyPlaceDao.deleteHappyPlace(happyPlace)
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