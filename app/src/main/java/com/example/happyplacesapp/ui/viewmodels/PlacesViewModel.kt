package com.example.happyplacesapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.happyplacesapp.data.database.HappyPlace
import com.example.happyplacesapp.data.repository.HappyPlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlacesViewModel(private val repository: HappyPlaceRepository) : ViewModel() {

    private val _places = MutableStateFlow<List<HappyPlace>>(emptyList())
    val places: StateFlow<List<HappyPlace>> = _places.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadPlaces()
    }

    private fun loadPlaces() {
        viewModelScope.launch {
            repository.getAllPlaces().collect { placesList ->
                _places.value = placesList
            }
        }
    }

    fun insertPlace(place: HappyPlace) {
        viewModelScope.launch {
            repository.insertPlace(place)
        }
    }

    fun updatePlace(place: HappyPlace) {
        viewModelScope.launch {
            repository.updatePlace(place)
        }
    }

    fun deletePlace(place: HappyPlace) {
        viewModelScope.launch {
            repository.deletePlace(place)
        }
    }

    fun searchPlaces(query: String) {
        if (query.isBlank()) {
            loadPlaces()
            return
        }
        viewModelScope.launch {
            repository.searchPlaces(query).collect { searchResults ->
                _places.value = searchResults
            }
        }
    }
}

class PlacesViewModelFactory(private val repository: HappyPlaceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlacesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlacesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}