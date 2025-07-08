package com.example.happyplacesapp.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import android.Manifest

class BackgroundLocationHelper(private val context: Context) {

    fun requestBackgroundLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {

                showBackgroundLocationRationale()
            }
        }
    }

    private fun showBackgroundLocationRationale() {
        AlertDialog.Builder(context)
            .setTitle("Background Location")
            .setMessage("Für eine bessere Nutzererfahrung benötigt die App Zugriff auf Ihren Standort auch im Hintergrund.")
            .setPositiveButton("Einstellungen") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Abbrechen", null)
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }
}