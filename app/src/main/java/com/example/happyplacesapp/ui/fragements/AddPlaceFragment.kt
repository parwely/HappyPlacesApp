package com.example.happyplacesapp.ui.fragements

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.happyplacesapp.R

class AddPlaceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)  // RICHTIG - Layout-Ressource
    }
}