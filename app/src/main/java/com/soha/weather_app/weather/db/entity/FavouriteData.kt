package com.soha.weather_app.weather.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.soha.weather_app.weather.db.model.Daily

@Entity(
    tableName = "favouriteData"
)
data class FavouriteData(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("daily")
    val daily: List<Daily>
    )