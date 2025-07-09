package com.example.happyplacesapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.happyplacesapp.data.database.HappyPlace
import com.example.happyplacesapp.data.repository.HappyPlaceRepository
import kotlinx.coroutines.launch

class HappyPlaceViewModel(private val repository: HappyPlaceRepository) : ViewModel() {

    val allPlaces: LiveData<List<HappyPlace>> = repository.allPlaces

    fun insertPlace(place: HappyPlace) = viewModelScope.launch {
        repository.insert(place)
    }

    fun updatePlace(place: HappyPlace) = viewModelScope.launch {
        repository.update(place)
    }

    fun deletePlace(place: HappyPlace) = viewModelScope.launch {
        repository.delete(place)
    }

    suspend fun getPlaceById(id: Long): HappyPlace? {
        return repository.getPlaceById(id)
    }

    suspend fun searchPlaces(query: String): List<HappyPlace> {
        return repository.searchPlaces("%$query%")
    }

    suspend fun getPlacesByCategory(category: String): List<HappyPlace> {
        return repository.getPlacesByCategory(category)
    }
}