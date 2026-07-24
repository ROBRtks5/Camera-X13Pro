package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_records")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val filePath: String,
    val timestamp: Long = System.currentTimeMillis(),
    val modeName: String,
    val focalLengthDesc: String,
    val zoomFactor: Float,
    val iso: Int,
    val shutterSpeed: String,
    val ev: Float,
    val focusDistanceCm: Float,
    val leicaProfileName: String,
    val note: String = "",
    val rating: Int = 5,
    val isFavorite: Boolean = false,
    val isFramed: Boolean = true
)
