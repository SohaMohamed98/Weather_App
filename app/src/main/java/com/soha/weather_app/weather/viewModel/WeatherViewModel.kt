package com.soha.weather_app.weather.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.soha.weather_app.weather.db.entity.WeatherResponse
import com.soha.weather_app.weather.db.Resource
import com.soha.weather_app.weather.db.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var sp: SharedPreferences
    private val newRepo: Repository
    val locationViewModel = SettingViewModel(application)

    val checkRoom=MutableLiveData<Boolean>()

    init {
        newRepo = Repository()
        sp = PreferenceManager.getDefaultSharedPreferences(getApplication())
    }

    val lat1 = sp.getString("lat", "")
    val lon1 = sp.getString("lon", "")
    val temp = sp.getString("temp", "")
    val long = sp.getString("lang", "")

    val weatherLiveData = MutableLiveData<Resource<WeatherResponse>>()
    val weatherFromRoomLiveData = MutableLiveData<Resource<WeatherResponse>>()

    fun getWeatherAPIData(
        context: Context, lat: String = lat1.toString(), lon: String = lon1.toString(),
        units: String = temp.toString(), long: String = this.long.toString(),
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            weatherLiveData.postValue(Resource.Loading())
            try {
                if (hasInternetConnection(context)) {
                    val response = newRepo.retrofitWeatherCall(lat, lon, units, long)
                    weatherLiveData.postValue(handleGetWeatherApiData(response, context)!!)

                } else {
                    weatherLiveData.postValue(Resource.Error("No internet connection"))

                }
                val weather = newRepo.getWeatherFromRoom(context)


                weatherFromRoomLiveData.postValue(handleGetWeatherFromRoom(weather)!!)

            } catch (t: Throwable) {
                when (t) {
                    is IOException -> weatherLiveData.postValue(Resource.Error("Network failuar"))
                    else -> weatherLiveData.postValue(Resource.Error("Conversion Error" + t))
                }
            }
        }
    }


    private fun handleGetWeatherFromRoom(weather: WeatherResponse): Resource<WeatherResponse>? {
        if (weather != null) {
            return Resource.Success(weather)
        }
        checkRoom.postValue(true)
        return Resource.Error("Room is empty")
    }


    private suspend fun handleGetWeatherApiData(
        response: retrofit2.Response<WeatherResponse>,
        context: Context,
    ): Resource<WeatherResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                newRepo.insertWeatherToRoom(context, it)
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun hasInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activityNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activityNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}