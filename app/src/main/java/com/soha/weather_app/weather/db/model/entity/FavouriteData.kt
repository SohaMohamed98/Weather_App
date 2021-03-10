package com.soha.weather_app.weather.db.model.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "favouriteData"
)
data class FavouriteData(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double

    )