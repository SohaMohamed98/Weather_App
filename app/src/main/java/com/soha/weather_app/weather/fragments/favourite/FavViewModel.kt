package com.soha.weather_app.weather.fragments.favourite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.soha.weather_app.weather.db.Repository
import com.soha.weather_app.weather.db.Resource
import com.soha.weather_app.weather.db.models.DailyModel.WeatherResponse
import com.soha.weather_app.weather.db.models.currentModel.CurrentResponse
import com.soha.weather_app.weather.db.models.currentModel.WeatherX
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch

class FavViewModel (application: Application): AndroidViewModel(application){

    var readAllLiveData = MutableLiveData<Resource<List<CurrentResponse>>>()
    lateinit var repo: Repository
   init {

       repo= Repository()
       CoroutineScope(Dispatchers.IO).launch {
           val weather = repo.getAllCureentListData(getApplication())
           readAllLiveData.postValue(handleGetWeatherFromRoomList(weather)!!)

       }
   }

    private fun handleGetWeatherFromRoomList(weather: List<CurrentResponse>): Resource<List<CurrentResponse>>? {
        if(weather != null){
            return Resource.Success(weather)
        }
        return Resource.Error("Room is empty")
    }

}
