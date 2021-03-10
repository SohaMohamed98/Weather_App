package com.soha.weather_app.weather.db.model

import com.google.gson.annotations.SerializedName

class Alert (
    @SerializedName("sender_name")
    val senderName: String,
    @SerializedName("start")
    val start: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("end")
    val end: Int,
    @SerializedName("event")
    val event: String
)