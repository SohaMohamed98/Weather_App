package com.soha.weather_app.db.Local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.soha.weather_app.weather.db.models.weatherModel.Weather
import com.soha.weather_app.weather.db.models.weatherModel.*
import com.soha.weather_app.weather.db.models.currentModel.*
import java.lang.reflect.Type

class TypeConvertDataBase {
    @TypeConverter
    fun fromCurrent(currentValues: Current?): String? {
        if (currentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Current>() {}.type
        return gson.toJson(currentValues, type) //json
    }

    @TypeConverter
    fun toCurrent(currentString: String?): Current? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Current>() {}.type
        return gson.fromJson<Current>(currentString, type)
    }
    //===================================================================

    @TypeConverter
    fun fromWeatherX(currentValues: List<WeatherX?>?): String? {
        if (currentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<WeatherX?>?>() {}.type
        return gson.toJson(currentValues, type) //json
    }

    @TypeConverter
    fun toWeatherX(currentString: String?): List<WeatherX?>? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<WeatherX?>?>() {}.type
        return gson.fromJson<List<WeatherX?>?>(currentString, type)
    }


//===============================================================================

    @TypeConverter
    fun fromDaily(currentValues: List<Daily?>?): String? {
        if (currentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily?>?>() {}.type
        return gson.toJson(currentValues, type) //json
    }

    @TypeConverter
    fun toDaily(currentString: String?): List<Daily?>? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily?>?>() {}.type
        return gson.fromJson<List<Daily?>?>(currentString, type)
    }


    //=======================================================================

    @TypeConverter
    fun fromTemp(currentValues: Temp?): String? {
        if (currentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Temp>() {}.type
        return gson.toJson(currentValues, type) //json
    }

    @TypeConverter
    fun toTemp(currentString: String?): Temp? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Temp>() {}.type
        return gson.fromJson<Temp>(currentString, type)
    }
    //=======================================================================

    @TypeConverter
    fun fromFeelsLike(currentValues: FeelsLike?): String? {
        if (currentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<FeelsLike>() {}.type
        return gson.toJson(currentValues, type) //json
    }

    @TypeConverter
    fun toFeelsLike(currentString: String?): FeelsLike? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<FeelsLike>() {}.type
        return gson.fromJson<FeelsLike>(currentString, type)
    }

    //=======================================================================

    @TypeConverter
    fun fromHourly(currentValues: List<Hourly?>?): String? {
        if (currentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Hourly?>?>() {}.type
        return gson.toJson(currentValues, type) //json
    }

    @TypeConverter
    fun toHourly(currentString: String?): List<Hourly?>? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Hourly?>?>() {}.type
        return gson.fromJson<List<Hourly?>?>(currentString, type)
    }


    //============================================================================

    @TypeConverter
    fun fromMinutely(currentValues: List<Minutely?>?): String? {
        if (currentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Minutely?>?>() {}.type
        return gson.toJson(currentValues, type) //json
    }

    @TypeConverter
    fun toMinutely(currentString: String?): List<Minutely?>? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Minutely?>?>() {}.type
        return gson.fromJson<List<Minutely?>?>(currentString, type)
    }
//////////////////////////////////////////////////////////////////////////////////

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
    fun toClouds(currentString: String?): Clouds? {
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
    fun toCoord(currentString: String?): Coord? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Coord?>() {}.type
        return gson.fromJson<Coord?>(currentString, type)
    }

    //===================================================================
    @TypeConverter
    fun fromMain(currentValues: Main?): String? {
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


}