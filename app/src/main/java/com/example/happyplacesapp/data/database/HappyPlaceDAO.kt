import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HappyPlaceDao {
    @Query("SELECT * FROM happy_places ORDER BY dateAdded DESC")
    fun getAllPlaces(): Flow<List<HappyPlace>>

    @Query("SELECT * FROM happy_places WHERE id = :id")
    suspend fun getPlaceById(id: Int): HappyPlace?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place: HappyPlace): Long

    @Update
    suspend fun updatePlace(place: HappyPlace)

    @Delete
    suspend fun deletePlace(place: HappyPlace)

    @Query("SELECT * FROM happy_places WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchPlaces(query: String): Flow<List<HappyPlace>>
}