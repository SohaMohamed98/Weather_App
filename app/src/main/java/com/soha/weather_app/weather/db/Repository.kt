package com.soha.weather_app.weather.db

import android.content.Context
import com.soha.weather_app.db.Local.WeatherDatabase
import com.soha.weather_app.db.remotely.RetrofitInstance
import com.soha.weather_app.weather.db.entity.WeatherResponse
import com.soha.weather_app.weather.db.entity.FavouriteData


public class Repository {

    public suspend fun retrofitWeatherCall(lat:String="31.25654", lon:String="32.28411",
                                           units:String="metric", long:String="ar") =
        RetrofitInstance.api.getWeatherData(lat,lon,units,long)

    public suspend fun insertWeatherToRoom(context: Context, weather: WeatherResponse) {
        WeatherDatabase.getInstance(context).getWeatherDao().upsert(weather)
    }

    public suspend fun getWeatherFromRoom(context: Context) =
        WeatherDatabase.getInstance(context).getWeatherDao().getAllWeathers()

    /////////////////////////////////////////////////////////////////////////////////////////////////



    public suspend fun retrofitFavCall(lat:String="31.25654", lon:String="32.28411",
                                           units:String="metric", long:String="ar")=
        RetrofitInstance.api.getFavData(lat,lon,units,long)

    public suspend fun insertFavWeatherToRoom(context: Context, weather: FavouriteData) {
        WeatherDatabase.getInstance(context).getWeatherDao().insertFavWeatherData(weather)
    }

    public suspend fun getFavWeatherFromRoom(context: Context) =
        WeatherDatabase.getInstance(context).getWeatherDao().getFavWetherData()










}