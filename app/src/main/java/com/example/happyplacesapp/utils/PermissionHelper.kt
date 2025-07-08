package com.example.happyplacesapp.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.Manifest

class PermissionHelper {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private const val BACKGROUND_LOCATION_REQUEST_CODE = 1002
    }

    fun checkLocationPermissions(context: Context): Boolean {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestLocationPermissions(fragment: Fragment) {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        fragment.requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE)
    }

    fun checkBackgroundLocationPermission(context: Context): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun requestBackgroundLocationPermission(fragment: Fragment) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            fragment.requestPermissions(
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                BACKGROUND_LOCATION_REQUEST_CODE
            )
        }
    }
}