package com.soha.weather_app.weather.db.model.currentModel


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Int
)