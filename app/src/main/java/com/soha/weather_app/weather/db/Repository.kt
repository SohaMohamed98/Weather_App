package com.soha.weather_app.weather.db

import android.content.Context
import com.soha.weather_app.db.Local.WeatherDatabase
import com.soha.weather_app.db.remotely.RetrofitInstance
import com.soha.weather_app.weather.db.models.weatherModel.WeatherResponse
import com.soha.weather_app.weather.db.models.currentModel.CurrentResponse
import com.soha.weather_app.weather.db.models.currentModel.FavCurrent
import com.soha.weather_app.weather.db.models.weatherModel.FavouriteData


public class Repository {

    public suspend fun retrofitWeatherCall(lat:String="31.25654", lon:String="32.28411",
                                           units:String="metric", long:String="ar") =
        RetrofitInstance.getWeatherService().getWeatherData(lat,lon,units,long)

    public suspend fun insertWeatherToRoom(context: Context, weather: WeatherResponse) {
        WeatherDatabase.getInstance(context).getWeatherDao().upsert(weather)
    }

    public suspend fun getWeatherFromRoom(context: Context) =
        WeatherDatabase.getInstance(context).getWeatherDao().getAllWeathers()
///////////////////////////////////////////////////////////////////////////////////////

    public suspend fun retrofitCurrentCall(lat:String="31.25654", lon:String="32.28411",
                                           units:String="metric", long:String="ar")=
        RetrofitInstance.getWeatherService().getCurrentData(lat,lon,units,long)

    public suspend fun insertCurrentToRoom(context: Context, current: CurrentResponse) {
        WeatherDatabase.getInstance(context).getWeatherDao().insertCurrent(current)
    }

    public suspend fun getCurrentFromRoom(context: Context) =
        WeatherDatabase.getInstance(context).getWeatherDao().getAllCurrent()

    /////////////////////////////////////////////////////////////////////////////////////////////////

    public suspend fun retrofitCurrentFavCall(lat:String="31.25654", lon:String="32.28411",
                                           units:String="metric", long:String="ar")=
        RetrofitInstance.getWeatherService().getCurrentFavData(lat,lon,units,long)

    public suspend fun insertFavCurrentToRoom(context: Context, weather: FavCurrent) {
        WeatherDatabase.getInstance(context).getWeatherDao().insertFavCurrentData(weather)
    }
    public suspend fun getFavCurrentFromRoom(context: Context) =
        WeatherDatabase.getInstance(context).getWeatherDao().getFavCurrentData()

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public suspend fun retrofitFavCall(lat:String="31.25654", lon:String="32.28411",
                                           units:String="metric", long:String="ar")=
        RetrofitInstance.getWeatherService().getFavData(lat,lon,units,long)

    public suspend fun insertFavWeatherToRoom(context: Context, weather: FavouriteData) {
        WeatherDatabase.getInstance(context).getWeatherDao().insertFavWeatherData(weather)
    }

    public suspend fun getFavWeatherFromRoom(context: Context) =
        WeatherDatabase.getInstance(context).getWeatherDao().getFavWetherData()










}