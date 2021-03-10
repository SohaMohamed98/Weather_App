package com.soha.weather_app.weather.db.model


import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Double,
    @SerializedName("main")
    val main: String
)