package com.soha.weather_app.weather.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.soha.weather_app.db.Local.WeatherDatabase
import com.soha.weather_app.db.remotely.RetrofitInstance
import com.soha.weather_app.db.remotely.WeatherAPI
import com.soha.weather_app.utils.Constants.Companion.API_KEY
import com.soha.weather_app.weather.db.entity.AlertEntity
import com.soha.weather_app.weather.db.entity.WeatherResponse
import com.soha.weather_app.weather.db.entity.FavouriteData
import com.soha.weather_app.weather.db.model.GeoModel.GeoModel
import kotlinx.coroutines.*
import retrofit2.Call
import java.lang.Exception


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


    public suspend fun deleteFav(favData: FavouriteData, context: Context) =
        WeatherDatabase.getInstance(context).getWeatherDao().deleteFav(favData)


/////////////////////////////////////////////////////////////////


    suspend fun getSearchLocation(location: String) =
       RetrofitInstance.api.getPlaceData(citName = location, API_KEY)

////////////////////////////////////////

  /*  public fun getPlace(location: String): MutableLiveData<GeoModel> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = getSearchLocation(location = location).execute()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        locationMutableLiveData.value = response.body()!!.get(0)
                    }
                }
            } catch (e: Exception) {
            }
        }

        return locationMutableLiveData
    }*/


    public suspend fun addAlert(alertDatabase: AlertEntity, context: Context) {
        val database = WeatherDatabase.getInstance(context).getWeatherDao().insertAlert(alertDatabase)
    }

    public fun getAlert(context: Context): LiveData<MutableList<AlertEntity>> =
         WeatherDatabase.getInstance(context).getWeatherDao().getAlerts()

    public suspend fun deleteAlert(alertDatabase: AlertEntity, context: Context) =
        WeatherDatabase.getInstance(context).getWeatherDao().deleteAlert(alertDatabase)

    }
