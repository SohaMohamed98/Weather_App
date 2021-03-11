package com.soha.weather_app.weather.db.model.GeoModel

import com.google.gson.annotations.SerializedName

data class GeoModel(
    @SerializedName("name")
    val name : String,
    @SerializedName("local_names")
    val local_names : Local_names,
    @SerializedName("lat")
    val lat : Double,
    @SerializedName("lon")
    val lon : Double,
    @SerializedName("country")
    val country : String
)