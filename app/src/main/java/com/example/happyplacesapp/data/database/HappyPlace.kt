package com.example.happyplacesapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "happy_places_table")
data class HappyPlace(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "location")
    val location: String = "",

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @ColumnInfo(name = "imagePath")
    val imagePath: String = "",

    @ColumnInfo(name = "notes")
    val notes: String = "",

    @ColumnInfo(name = "category")
    val category: String = "Allgemein",

    @ColumnInfo(name = "dateAdded")
    val dateAdded: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "isFromCurrentLocation")
    val isFromCurrentLocation: Boolean = false,

    @ColumnInfo(name = "address")
    val address: String = ""
) : Parcelable
