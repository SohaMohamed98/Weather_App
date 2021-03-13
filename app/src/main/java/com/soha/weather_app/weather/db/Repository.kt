package com.soha.weather_app.weather.db

import android.content.Context
import androidx.lifecycle.LiveData
import com.soha.weather_app.weather.db.Local.WeatherDatabase
import com.soha.weather_app.weather.db.entity.AlertEntity
import com.soha.weather_app.weather.db.entity.CustomAlert
import com.soha.weather_app.weather.db.entity.WeatherResponse
import com.soha.weather_app.weather.db.entity.FavouriteData
import com.soha.weather_app.weather.db.remotely.RetrofitInstance


public class Repository {

    public suspend fun retrofitWeatherCall(lat:String, lon:String,
                                           units:String="metric", long:String="en") =
        RetrofitInstance.api.getWeatherData(lat,lon,units,long)

    public suspend fun insertWeatherToRoom(context: Context, weather: WeatherResponse) {
        WeatherDatabase.getInstance(context).getWeatherDao().upsert(weather)
    }

    public suspend fun getWeatherFromRoom(context: Context) =
        WeatherDatabase.getInstance(context).getWeatherDao().getAllWeathers()

    /////////////////////////////////////////////////////////////////////////////////////////////////



    public suspend fun retrofitFavCall(lat:String, lon:String,
                                           units:String="metric", long:String="ar")=
        RetrofitInstance.api.getFavData(lat,lon,units,long)

    public suspend fun insertFavWeatherToRoom(context: Context, weather: FavouriteData) {
        WeatherDatabase.getInstance(context).getWeatherDao().insertFavWeatherData(weather)
    }

    public suspend fun getFavWeatherFromRoom(context: Context) =
        WeatherDatabase.getInstance(context).getWeatherDao().getFavWetherData()


    public suspend fun deleteFav(favData: FavouriteData, context: Context) =
        WeatherDatabase.getInstance(context).getWeatherDao().deleteFav(favData)

   //=======================================================================================

    public suspend fun addAlert(alertDatabase: AlertEntity, context: Context) =
         WeatherDatabase.getInstance(context).getWeatherDao().insertAlert(alertDatabase)


    public fun getAlert(context: Context): LiveData<MutableList<AlertEntity>> =
         WeatherDatabase.getInstance(context).getWeatherDao().getAlerts()

    public suspend fun deleteAlert(alertDatabase: AlertEntity, context: Context) =
        WeatherDatabase.getInstance(context).getWeatherDao().deleteAlert(alertDatabase)

    //============================================================================================

    public suspend fun addCustomAlert(alertDatabase: CustomAlert, context: Context) =
        WeatherDatabase.getInstance(context).getWeatherDao().insertCustomAlert(alertDatabase)


    public fun getCustomAlert(context: Context): LiveData<MutableList<CustomAlert>> =
        WeatherDatabase.getInstance(context).getWeatherDao().getCustomAlerts()

    public suspend fun deleteCustomAlert(alertDatabase: CustomAlert, context: Context) =
        WeatherDatabase.getInstance(context).getWeatherDao().deleteCustomAlert(alertDatabase)
    }
