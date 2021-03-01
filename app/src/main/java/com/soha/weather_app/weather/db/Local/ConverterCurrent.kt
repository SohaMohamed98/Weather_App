package com.soha.weather_app.weather.db.Local

import androidx.room.CoroutinesRoom
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.soha.weather_app.utils.model.Minutely
import com.soha.weather_app.utils.model.Weather
import com.soha.weather_app.weather.db.model.currentModel.*
import java.lang.reflect.Type

class ConverterCurrent {
    @TypeConverter
    fun fromClouds(currentValues: Clouds?): String? {
        if (currentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Clouds?>() {}.type
        return gson.toJson(currentValues, type) //json
    }

    @TypeConverter
    fun toClouds(currentString: String?):Clouds? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Clouds?>() {}.type
        return gson.fromJson<Clouds?>(currentString, type)
    }

    //==========================================================================

    @TypeConverter
    fun fromCoord(currentValues: Coord?): String? {
        if (currentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Coord?>() {}.type
        return gson.toJson(currentValues, type) //json
    }

    @TypeConverter
    fun toCoord(currentString: String?):Coord? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Coord?>() {}.type
        return gson.fromJson<Coord?>(currentString, type)
    }

    //===================================================================
    @TypeConverter
    fun fromMain(currentValues:Main?): String? {
        if (currentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Main?>() {}.type
        return gson.toJson(currentValues, type) //json
    }

    @TypeConverter
    fun toMain(currentString: String?): Main? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Main?>() {}.type
        return gson.fromJson<Main?>(currentString, type)
    }

//===========================================================================================

    @TypeConverter
    fun fromSys(currentValues: Sys?): String? {
        if (currentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Sys?>() {}.type
        return gson.toJson(currentValues, type) //json
    }

    @TypeConverter
    fun toSys(currentString: String?): Sys? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Sys?>() {}.type
        return gson.fromJson<Sys?>(currentString, type)
    }

    //=============================================================================

    @TypeConverter
    fun fromWind(currentValues: Wind?): String? {
        if (currentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Wind?>() {}.type
        return gson.toJson(currentValues, type) //json
    }

    @TypeConverter
    fun toWind(currentString: String?): Wind? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Wind>() {}.type
        return gson.fromJson<Wind?>(currentString, type)
    }

}

//=====================================================================================

@TypeConverter
fun fromWeather(currentValues: List<Weather?>?): String? {
    if (currentValues == null) {
        return null
    }
    val gson = Gson()
    val type: Type = object : TypeToken<List<Weather?>?>() {}.type
    return gson.toJson(currentValues, type) //json
}

@TypeConverter
fun toWeather(currentString: String?): List<Weather?>? {
    if (currentString == null) {
        return null
    }
    val gson = Gson()
    val type: Type = object : TypeToken<List<Weather?>?>() {}.type
    return gson.fromJson<List<Weather?>?>(currentString, type)
}
