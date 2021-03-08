package com.soha.weather_app.weather.db

import android.content.Context
import com.soha.weather_app.db.Local.WeatherDatabase
import com.soha.weather_app.db.remotely.RetrofitInstance
import com.soha.weather_app.weather.db.models.DailyModel.WeatherResponse
import com.soha.weather_app.weather.db.models.currentModel.CurrentResponse


public class Repository {

    public suspend fun retrofitWeatherCall(lat:String="31.25654", lon:String="32.28411",
                                           units:String="metric", long:String="ar") =
        RetrofitInstance.getWeatherService().getWeatherData(lat,lon,units,long)

    public suspend fun retrofitCurrentCall(lat:String="31.25654", lon:String="32.28411",
                                           units:String="metric", long:String="ar")=
        RetrofitInstance.getWeatherService().getCurrentData(lat,lon,units,long)



    public suspend fun insertWeatherToRoom(context: Context, weather: WeatherResponse) {
        WeatherDatabase.getInstance(context).getWeatherDao().upsert(weather)
    }

    public suspend fun getWeatherFromRoom(context: Context) =
        WeatherDatabase.getInstance(context).getWeatherDao().getAllWeathers()


    public suspend fun insertCurrentToRoom(context: Context, current: CurrentResponse) {
        WeatherDatabase.getInstance(context).getWeatherDao().insertCurrent(current)
    }

    public suspend fun getCurrentFromRoom(context: Context) =
        WeatherDatabase.getInstance(context).getWeatherDao().getAllCurrent()

    fun getAllData(context: Context): List<WeatherResponse> =
        WeatherDatabase.getInstance(context).getWeatherDao().getAllListWeatherData()

    fun getAllCureentListData(context: Context): List<CurrentResponse> =
        WeatherDatabase.getInstance(context).getWeatherDao().getAllCurrentListData()



}