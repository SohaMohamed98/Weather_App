package com.soha.weather_app.weather.db

import android.content.Context
import com.soha.weather_app.db.Local.WeatherDatabase
import com.soha.weather_app.db.remotely.RetrofitInstance
import com.soha.weather_app.utils.model.WeatherResponse


public class Repository {

     public suspend fun retrofitWeatherCall() =
        RetrofitInstance.api.getWeatherData()

     public suspend fun insertWeatherToRoom(context: Context ,weather: WeatherResponse){
         WeatherDatabase.getInstance(context).getWeatherDao().upsert(weather)
     }
     public suspend fun getWeatherFromRoom(context: Context) =
         WeatherDatabase.getInstance(context).getWeatherDao().getAllWeathers()
 }
