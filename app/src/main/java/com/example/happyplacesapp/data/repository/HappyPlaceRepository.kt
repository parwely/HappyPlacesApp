package com.example.happyplacesapp.data.repository

import androidx.lifecycle.LiveData
import com.example.happyplacesapp.data.database.HappyPlace
import com.example.happyplacesapp.data.database.HappyPlaceDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HappyPlaceRepository(private val happyPlaceDao: HappyPlaceDao) {

    val allPlaces: LiveData<List<HappyPlace>> = happyPlaceDao.getAllHappyPlaces()

    suspend fun insert(place: HappyPlace): Long {
        return withContext(Dispatchers.IO) {
            happyPlaceDao.insert(place)
        }
    }

    suspend fun update(place: HappyPlace): Int {
        return withContext(Dispatchers.IO) {
            happyPlaceDao.update(place)
        }
    }

    suspend fun delete(place: HappyPlace): Int {
        return withContext(Dispatchers.IO) {
            happyPlaceDao.delete(place)
        }
    }

    suspend fun getPlaceById(id: Long): HappyPlace? {
        return withContext(Dispatchers.IO) {
            happyPlaceDao.getHappyPlaceById(id)
        }
    }

    suspend fun searchPlaces(searchQuery: String): List<HappyPlace> {
        return withContext(Dispatchers.IO) {
            happyPlaceDao.searchHappyPlaces(searchQuery)
        }
    }

    suspend fun getPlacesByCategory(category: String): List<HappyPlace> {
        return withContext(Dispatchers.IO) {
            happyPlaceDao.getHappyPlacesByCategory(category)
        }
    }

    suspend fun deleteAllPlaces(): Int {
        return withContext(Dispatchers.IO) {
            happyPlaceDao.deleteAllHappyPlaces()
        }
    }
}