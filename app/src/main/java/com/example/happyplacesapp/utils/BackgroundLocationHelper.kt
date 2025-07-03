import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.audiofx.BassBoost
import android.net.Uri
import android.os.Build
import androidx.core.content.ContextCompat
import java.util.jar.Manifest

class BackgroundLocationHelper(private val context: Context) {

    fun requestBackgroundLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {

                // Erkläre dem Nutzer warum Background Location benötigt wird
                showBackgroundLocationRationale()
            }
        }
    }

    private fun showBackgroundLocationRationale() {
        // AlertDialog oder ähnliches für User Education
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
        val intent = Intent(BassBoost.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }
}