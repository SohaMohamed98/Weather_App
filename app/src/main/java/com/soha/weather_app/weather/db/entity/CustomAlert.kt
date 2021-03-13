package com.soha.weather_app.weather.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "custom_alert_table")
class CustomAlert (
    @PrimaryKey(autoGenerate = true)
    var id: Int ,
    @ColumnInfo
    val description: String,
    val icon: String,
    val main: String,
    val dt: Int,
    val temp: Double,
    val sunrise: Double,
    val sunset: Double
)