package com.soha.weather_app.db.remotely

import com.soha.weather_app.utils.Constants.Companion.API_KEY
import com.soha.weather_app.weather.db.models.weatherModel.WeatherResponse
import com.soha.weather_app.weather.db.models.currentModel.CurrentResponse
import com.soha.weather_app.weather.db.models.currentModel.FavCurrent
import com.soha.weather_app.weather.db.models.weatherModel.FavouriteData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    //ar  en lang

    @GET("onecall")
    suspend fun getWeatherData(
        @Query("lat") lat: String = "31.25654",
        @Query("lon") lon: String = "32.28411",
        @Query("units") units: String = "metric",//metric: Celsius, imperial: Fahrenheit.
        @Query("lang") lang: String = "ar",  //metric: metre/sec, imperial: miles/hour
        // @Query("exclude") exclude: String="current",
        @Query("APPID") app_id: String = API_KEY,
    ): Response<WeatherResponse>

    @GET("weather")
    suspend fun getCurrentData(
        @Query("lat") lat: String="31.25654",
        @Query("lon") lon: String="32.28411",
        @Query("units") units: String="metric",
        @Query("lang") lang: String="ar",
        @Query("APPID") app_id: String = API_KEY,
    ): Response<CurrentResponse>

    @GET("weather")
    suspend fun getCurrentFavData(
        @Query("lat") lat: String="31.25654",
        @Query("lon") lon: String="32.28411",
        @Query("units") units: String="metric",
        @Query("lang") lang: String="ar",
        @Query("APPID") app_id: String = API_KEY,
    ): Response<FavCurrent>


    @GET("onecall")
    suspend fun getFavData(
        @Query("lat") lat: String = "31.25654",
        @Query("lon") lon: String = "32.28411",
        @Query("units") units: String = "metric",//metric: Celsius, imperial: Fahrenheit.
        @Query("lang") lang: String = "ar",  //metric: metre/sec, imperial: miles/hour
        // @Query("exclude") exclude: String="current",
        @Query("APPID") app_id: String = API_KEY,
    ): Response<FavouriteData>

}