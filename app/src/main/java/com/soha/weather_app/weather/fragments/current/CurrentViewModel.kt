package com.soha.weather_app.weather.fragments.current

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soha.weather_app.weather.db.Repository
import com.soha.weather_app.weather.db.Resource
import com.soha.weather_app.weather.db.models.currentModel.CurrentResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class CurrentViewModel: ViewModel() {
    private lateinit var repo: Repository
    private val homeViewModel = HomeViewModel()

    init {
        repo = Repository()
    }


    val currentLiveData = MutableLiveData<Resource<CurrentResponse>>()
    val currentLiveDataFromRoom = MutableLiveData<Resource<CurrentResponse>>()
    fun getCurrentAPIData(context: Context) = CoroutineScope(Dispatchers.IO).launch {

        currentLiveData.postValue(Resource.Loading())


        try {
            if (homeViewModel.hasInternetConnection(context)) {
                val response2 = repo.retrofitCurrentCall()
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
    private suspend fun handleGetCurrentApiData(response: Response<CurrentResponse>, context: Context):
            Resource<CurrentResponse>? {

        if(response.isSuccessful){
            response.body()?.let {
                repo.insertCurrentToRoom(context,it)
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())

    }

    private fun handleGetCurrentFromRoom(current: CurrentResponse): Resource<CurrentResponse>? {
        if(current != null){
            return Resource.Success(current)
        }
        return Resource.Error("Room is empty")
    }



}