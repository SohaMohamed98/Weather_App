package com.soha.weather_app.weather.db.models.DailyModel


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "weatherData"
)
data class WeatherResponse(
    @PrimaryKey
    val id : Int,
    @SerializedName("current")
    @ColumnInfo(name = "current")
    val current: Current,
    @SerializedName("daily")
    @ColumnInfo(name = "daily")
    val daily: List<Daily>,
    @SerializedName("hourly")
    @ColumnInfo(name = "hourly")
    val hourly: List<Hourly>,
    @SerializedName("lat")
    @ColumnInfo(name = "lat")
    val lat: Double,
    @SerializedName("lon")
    @ColumnInfo(name = "lon")
    val lon: Double,
    @SerializedName("minutely")
    @ColumnInfo(name = "minutely")
    val minutely: List<Minutely>,
    @SerializedName("timezone")
    @ColumnInfo(name = "timezone")
    val timezone: String,
    @SerializedName("timezone_offset")
    @ColumnInfo(name = "timezone_offset")
    val timezoneOffset: Double


    )