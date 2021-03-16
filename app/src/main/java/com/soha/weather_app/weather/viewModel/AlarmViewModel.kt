package com.soha.alert.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soha.weather_app.weather.db.Repository
import com.soha.weather_app.weather.db.Resource
import com.soha.weather_app.weather.db.entity.AlarmEntity
import com.soha.weather_app.weather.db.entity.WeatherResponse
import com.soha.weather_app.weather.db.model.Hourly
import com.soha.weather_app.weather.utils.dayConverter
import com.soha.weather_app.weather.utils.timeConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    private var repo: Repository

    val weatherFromRoomLiveData = MutableLiveData<Resource<WeatherResponse>>()
    init {
        repo = Repository()

    }

    fun getWeatherFromRoom(){
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getWeatherFromRoom(getApplication())
            weatherFromRoomLiveData.postValue(handleGetWeatherFromRoom(result)!!)
        }
    }

    private fun handleGetWeatherFromRoom(result: WeatherResponse): Resource<WeatherResponse>? {
        if (result != null) {
            return Resource.Success(result)
        }
        return Resource.Error("Room is empty")
    }


    fun convertAndCheck(date: String, startTime: String, endTime: String): Boolean {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
        var convertedDate: Date?
        var convertedDate2: Date?
        var convertedDate3: Date?
        try {
            convertedDate = dateFormat.parse(date)
            convertedDate2 = dateFormat.parse(startTime)
            convertedDate3 = dateFormat.parse(endTime)
            if ((convertedDate2.before(convertedDate)&&convertedDate3.after(convertedDate))
                ||convertedDate2.equals(convertedDate)||convertedDate3.equals(convertedDate)) {
                return true
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return false
    }


    fun search(alarmHours: List<Hourly>, startTime: String, endTime: String, event: String): Hourly? {
        var i: Int = 0
        while (i < alarmHours.size) {
            var timeInList = dayConverter(alarmHours.get(i).dt.toLong())
            if (convertAndCheck(timeInList!!, startTime!!, endTime)
                && alarmHours.get(i).weather.get(0).main.equals(event))
            {
                return alarmHours.get(i)
            } else {
                i++
            }
        }
        return null
    }




    suspend fun addAlarm(alertDatabase: AlarmEntity, context: Context) {
        return repo.addAlarm(alertDatabase, context)
    }

    fun getAlarm(context: Context): LiveData<MutableList<AlarmEntity>> {
        return repo.getAlarm(context)
    }

    suspend fun deleteAlarm(alertDatabase: AlarmEntity, context: Context) {
        return repo.deleteAlarm(alertDatabase, context)
    }



}