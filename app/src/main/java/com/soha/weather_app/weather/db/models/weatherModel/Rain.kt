package com.soha.weather_app.weather.db.models.weatherModel


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    val h: Double
)