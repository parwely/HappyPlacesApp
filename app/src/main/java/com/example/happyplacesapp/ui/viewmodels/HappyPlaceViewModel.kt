package com.example.happyplacesapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.happyplacesapp.data.database.HappyPlace
import com.example.happyplacesapp.data.database.HappyPlaceDatabase
import com.example.happyplacesapp.data.repository.HappyPlaceRepository
import kotlinx.coroutines.launch

class HappyPlaceViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HappyPlaceRepository
    val allPlaces: LiveData<List<HappyPlace>>

    init {
        val dao = HappyPlaceDatabase.getDatabase(application).happyPlaceDao()
        repository = HappyPlaceRepository(dao)
        allPlaces = repository.allPlaces
    }

    fun insert(place: HappyPlace) = viewModelScope.launch {
        repository.insert(place)
    }

    fun update(place: HappyPlace) = viewModelScope.launch {
        repository.update(place)
    }

    fun delete(place: HappyPlace) = viewModelScope.launch {
        repository.delete(place)
    }
}