package com.soha.weather_app.weather.fragments.current

import android.app.Application
import android.content.Context
import android.content.LocusId
import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.*
import com.soha.weather_app.weather.db.Repository
import com.soha.weather_app.weather.db.Resource
import com.soha.weather_app.weather.db.models.currentModel.CurrentResponse
import com.soha.weather_app.weather.fragments.setting.MapFragment.LocationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class CurrentViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var repo: Repository
    private val homeViewModel = WeatherViewModel(application)


    val locationViewModel = LocationViewModel(application)

    init {
        repo = Repository()
    }

    // val lon="30.12541"

    val lat = locationViewModel.getLatData().value
    val lon = locationViewModel.getLonData().value
    val temp = locationViewModel.getTempData().value
    val long = locationViewModel.getLonData().value



    val currentLiveData = MutableLiveData<Resource<CurrentResponse>>()
    val currentLiveDataFromRoom = MutableLiveData<Resource<CurrentResponse>>()

    //31.25654&lon=32.28411
    fun getCurrentAPIData(
        context: Context, lat: String = this.lat.toString(), lon: String = this.lon.toString(),
        units: String = this.temp.toString(), long: String = this.long.toString(),
    ) =
        CoroutineScope(Dispatchers.IO).launch {
            currentLiveData.postValue(Resource.Loading())
            try {
                if (homeViewModel.hasInternetConnection(context)) {
                    val response2 = repo.retrofitCurrentCall(lat, lon, units, long)
                    currentLiveData.postValue(handleGetCurrentApiData(response2, context)!!)

                } else {
                    currentLiveData.postValue(Resource.Error("No internet connection"))
                }
                val current = repo.getCurrentFromRoom(context)
                currentLiveDataFromRoom.postValue(handleGetCurrentFromRoom(current)!!)

            } catch (t: Throwable) {
                when (t) {
                    is IOException -> currentLiveData.postValue(Resource.Error("Network failuar"))
                    else -> currentLiveData.postValue(Resource.Error("Conversion Error" + t))
                }

            }
        }

    private suspend fun handleGetCurrentApiData(
        response: Response<CurrentResponse>,
        context: Context,
    ):
            Resource<CurrentResponse>? {

        if (response.isSuccessful) {
            response.body()?.let {
                repo.insertCurrentToRoom(context, it)
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())

    }

    private fun handleGetCurrentFromRoom(current: CurrentResponse): Resource<CurrentResponse>? {
        if (current != null) {
            return Resource.Success(current)
        }
        return Resource.Error("Room is empty")
    }


}