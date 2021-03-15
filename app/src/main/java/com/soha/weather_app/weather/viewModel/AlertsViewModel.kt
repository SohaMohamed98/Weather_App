package com.soha.alert.viewModel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import androidx.work.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.soha.weather_app.weather.db.Repository
import com.soha.weather_app.weather.db.entity.AlarmEntity
import com.soha.weather_app.weather.db.entity.AlertEntity
import com.soha.weather_app.weather.provider.AlertBuild
import com.soha.weather_app.weather.receiver.AlertReceiver
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

class AlertsViewModel(application: Application) : AndroidViewModel(application) {
    private var repo: Repository


    init {
        repo = Repository()

    }

    suspend fun addAlert(alertDatabase: AlertEntity, context: Context) {
        return repo.addAlert(alertDatabase, context)
    }

    fun getAlert(context: Context): LiveData<MutableList<AlertEntity>> {
        return repo.getAlert(context)
    }

    suspend fun deleteAlert(alertDatabase: AlertEntity, context: Context) {
        return repo.deleteAlert(alertDatabase, context)
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