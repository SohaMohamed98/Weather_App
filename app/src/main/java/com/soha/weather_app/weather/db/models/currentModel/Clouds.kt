package com.soha.weather_app.weather.db.models.currentModel


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Double
)