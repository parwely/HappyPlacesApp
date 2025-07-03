import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageHelper {
    companion object {
        fun saveImageToInternalStorage(context: Context, bitmap: Bitmap): String {
            val filename = "IMG_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, filename)

            try {
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                outputStream.flush()
                outputStream.close()
                return file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        }

        fun loadImageFromPath(imagePath: String): Bitmap? {
            return try {
                BitmapFactory.decodeFile(imagePath)
            } catch (e: Exception) {
                null
            }
        }

        // Android 8+ FileProvider für Kamera
        fun createImageFile(context: Context): File {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
            )
        }

        fun getImageUri(context: Context, file: File): Uri {
            return FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        }
    }
}

