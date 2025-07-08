package com.example.happyplacesapp.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(
    entities = [HappyPlace::class],
    version = 1,
    exportSchema = false
)
abstract class HappyPlaceDatabase : RoomDatabase() {

    abstract fun happyPlaceDao(): HappyPlaceDao

    companion object {
        @Volatile
        private var INSTANCE: HappyPlaceDatabase? = null

        fun getDatabase(context: Context): HappyPlaceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HappyPlaceDatabase::class.java,
                    "happy_places_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}