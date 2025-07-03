import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "happy_places")
data class HappyPlace(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val imagePath: String = "",
    val notes: String = "",
    val category: String = "Allgemein",
    val dateAdded: Long = System.currentTimeMillis()
)