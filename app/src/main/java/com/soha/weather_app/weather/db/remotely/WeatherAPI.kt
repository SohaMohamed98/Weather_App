package com.soha.weather_app.db.remotely

import com.soha.weather_app.utils.Constants.Companion.API_KEY
import com.soha.weatherapp.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    //ar  en lang

    @GET("onecall")
    suspend fun getWeatherData(@Query("lat") lat: String="31.25654",
                               @Query("lon") lon: String="32.28411",
                               @Query("units") units: String="metric",
                               @Query(" lang") lang: String="ar",
                              // @Query("exclude") exclude: String="current",
                               @Query("APPID") app_id: String = API_KEY): Response<WeatherResponse>


}