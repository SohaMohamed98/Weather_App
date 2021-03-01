package com.soha.weather_app.weather.fragments.current

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soha.weather_app.utils.model.WeatherResponse
import com.soha.weather_app.weather.db.Resource
import com.soha.weather_app.weather.db.Repository
import com.soha.weather_app.weather.db.model.currentModel.CurrentResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class HomeViewModel: ViewModel()  {
    private val newRepo : Repository
    init {
        newRepo = Repository()
    }

    val CurrentLiveData = MutableLiveData<Resource<CurrentResponse>>()
    val WeatherLiveData = MutableLiveData<Resource<WeatherResponse>>()
    val CurrentLiveDataFromRoom = MutableLiveData<Resource<CurrentResponse>>()
    val weatherFromRoomLiveData = MutableLiveData<Resource<WeatherResponse>>()

    fun getWeatherAPIData(context: Context) =  CoroutineScope(Dispatchers.IO).launch {
        WeatherLiveData.postValue(Resource.Loading())
        CurrentLiveData.postValue(Resource.Loading())
        try {
            if(hasInternetConnection(context)){
                val response = newRepo.retrofitWeatherCall()
                WeatherLiveData.postValue(handleGetWeatherApiData(response , context)!!)
                val response2 = newRepo.retrofitCurrentCall()
                CurrentLiveData.postValue(handleGetCurrentApiData(response2 , context)!!)
            }else{
                WeatherLiveData.postValue(Resource.Error("No internet connection"))
                CurrentLiveData.postValue(Resource.Error("No internet connection"))


            }
            val weather = newRepo.getWeatherFromRoom(context)
            val current = newRepo.getCurrentFromRoom(context)


            weatherFromRoomLiveData.postValue(handleGetWeatherFromRoom(weather)!!)

        }catch (t:Throwable){
            when(t){
                is IOException -> WeatherLiveData.postValue(Resource.Error("Network failuar"))
                else -> WeatherLiveData.postValue(Resource.Error("Conversion Error"+t))
            }
        }
    }

    private suspend fun handleGetCurrentApiData(response: Response<CurrentResponse>, context: Context): Resource<CurrentResponse>? {

        if(response.isSuccessful){
            response.body()?.let {
                newRepo.insertCurrentToRoom(context,it)
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())

    }

    private fun handleGetWeatherFromRoom(weather: WeatherResponse): Resource<WeatherResponse>? {
        if(weather != null){
            return Resource.Success(weather)
        }
        return Resource.Error("Room is empty")
    }


    private suspend fun handleGetWeatherApiData(
        response: retrofit2.Response<WeatherResponse>,
        context: Context
    ): Resource<WeatherResponse>? {
        if(response.isSuccessful){
            response.body()?.let {
                //  val current = it.current
                // val daily = it.daily
                // newRepo.insertcurrentWeatherToRoom(context,current)
                // newRepo.insertDailyWeatherToRoom(context, daily)
                newRepo.insertWeatherToRoom(context,it)
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(context: Context):Boolean{
        val connectivityManager = context.getSystemService(
                Context.CONNECTIVITY_SERVICE
        )as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activityNetwork = connectivityManager.activeNetwork?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activityNetwork)?:return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
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