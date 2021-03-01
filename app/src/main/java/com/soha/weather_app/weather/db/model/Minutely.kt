package com.soha.weatherapp.model


import com.google.gson.annotations.SerializedName

data class Minutely(
    @SerializedName("dt")
    val dt: Double,
    @SerializedName("precipitation")
    val precipitation: Double
)