package com.soha.alert.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.soha.weather_app.weather.db.Repository
import com.soha.weather_app.weather.db.entity.AlertEntity

class AlertsViewModel : ViewModel() {
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
}