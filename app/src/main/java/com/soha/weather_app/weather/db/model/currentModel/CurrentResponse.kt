package com.soha.weather_app.weather.db.model.currentModel


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.soha.weather_app.weather.db.Local.ConverterCurrent

@Entity(
    tableName = "currentData"
)
@TypeConverters(ConverterCurrent::class)
data class CurrentResponse(
    @PrimaryKey
    val num: Int,
    @SerializedName("base")
    @ColumnInfo(name = "base")
    val base: String,
    @SerializedName("clouds")
    @ColumnInfo(name = "clouds")
    val clouds: Clouds,
    @SerializedName("cod")
    @ColumnInfo(name = "cod")
    val cod: Double,
    @SerializedName("coord")
    @ColumnInfo(name = "coord")
    val coord: Coord,
    @SerializedName("dt")
    @ColumnInfo(name = "dt")
    val dt: Double,
    @SerializedName("id")
    @ColumnInfo(name = "id")
    val id: Double,
    @SerializedName("main")
    @ColumnInfo(name = "main")
    val main: Main,
    @SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String,
    @SerializedName("sys")
    @ColumnInfo(name = "sys")
    val sys: Sys,
    @SerializedName("timezone")
    @ColumnInfo(name = "timezone")
    val timezone: Double,
    @SerializedName("visibility")
    @ColumnInfo(name = "visibility")
    val visibility: Double,
    @SerializedName("weather")
    @ColumnInfo(name = "weather")
    val weather: List<Weather>,
    @SerializedName("wind")
    @ColumnInfo(name = "wind")
    val wind: Wind
)