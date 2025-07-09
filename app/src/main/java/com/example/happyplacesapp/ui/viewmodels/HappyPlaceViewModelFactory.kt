package com.example.happyplacesapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.happyplacesapp.data.repository.HappyPlaceRepository

class HappyPlaceViewModelFactory(private val repository: HappyPlaceRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HappyPlaceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HappyPlaceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}