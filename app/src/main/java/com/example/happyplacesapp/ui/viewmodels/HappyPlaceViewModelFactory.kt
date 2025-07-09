package com.example.happyplacesapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HappyPlaceViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HappyPlaceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HappyPlaceViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}