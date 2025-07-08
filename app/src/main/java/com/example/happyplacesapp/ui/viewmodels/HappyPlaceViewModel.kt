package com.example.happyplacesapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.happyplacesapp.data.database.HappyPlace
import com.example.happyplacesapp.data.repository.HappyPlaceRepository
import kotlinx.coroutines.launch

class HappyPlaceViewModel(private val repository: HappyPlaceRepository) : ViewModel() {

    val places: LiveData<List<HappyPlace>> = repository.getAllHappyPlaces()

    suspend fun insertPlace(place: HappyPlace): Long {
        return repository.insertHappyPlace(place)
    }

    suspend fun updatePlace(place: HappyPlace): Int {
        return repository.updateHappyPlace(place)
    }

    suspend fun deletePlace(place: HappyPlace): Int {
        return repository.deleteHappyPlace(place)
    }

    suspend fun getPlaceById(id: Long): HappyPlace? {
        return repository.getHappyPlaceById(id)
    }

    fun searchPlaces(query: String) = viewModelScope.launch {
        repository.searchHappyPlaces(query)
    }
}