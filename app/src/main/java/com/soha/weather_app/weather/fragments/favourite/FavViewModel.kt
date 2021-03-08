package com.soha.weather_app.weather.fragments.favourite

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.soha.weather_app.weather.db.Repository
import com.soha.weather_app.weather.db.Resource
import com.soha.weather_app.weather.db.models.currentModel.FavCurrent
import com.soha.weather_app.weather.fragments.setting.MapFragment.LocationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class FavViewModel (application: Application): AndroidViewModel(application){

    var favLiveData = MutableLiveData<Resource<FavCurrent>>()
    val favFromRoomLiveData = MutableLiveData<Resource<FavCurrent>>()
    private val newRepo: Repository
    val locationViewModel = LocationViewModel(application)

    init {
        newRepo = Repository()
    }

    val lat = locationViewModel.getLatData().value
    val lon = locationViewModel.getLonData().value
    val temp = locationViewModel.getTempData().value
    val long = locationViewModel.getLonData().value



    fun getFavAPIData(  context: Context, lat: String = this.lat.toString(), lon: String = this.lon.toString(),
                            units: String = this.temp.toString(), long: String = this.long.toString())
            = CoroutineScope(Dispatchers.IO).launch {
        favLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(context)) {
                val response = newRepo.retrofitCurrentFavCall(lat, lon, units, long)
                favLiveData.postValue(handleGetFavApiData(response, context)!!)

            } else {
                favLiveData.postValue(Resource.Error("No internet connection"))

            }
            val weather = newRepo.getFavCurrentFromRoom(context)


            favFromRoomLiveData.postValue(handleGetFavFromRoom(weather)!!)

        } catch (t: Throwable) {
            when (t) {
                is IOException -> favLiveData.postValue(Resource.Error("Network failuar"))
                else -> favLiveData.postValue(Resource.Error("Conversion Error" + t))
            }
        }
    }

    private fun handleGetFavFromRoom(weather: FavCurrent): Resource<FavCurrent>? {
        if (weather != null) {
            return Resource.Success(weather)
        }
        return Resource.Error("Room is empty")
    }


    private suspend fun handleGetFavApiData(
        response: retrofit2.Response<FavCurrent>,
        context: Context,
    ): Resource<FavCurrent>? {
        if (response.isSuccessful) {
            response.body()?.let {
                newRepo.insertFavCurrentToRoom(context, it)
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
