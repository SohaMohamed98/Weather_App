package com.soha.weather_app.db.Local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.soha.weather_app.weather.db.models.DailyModel.Daily
import com.soha.weather_app.weather.db.models.DailyModel.WeatherResponse
import com.soha.weather_app.weather.db.models.currentModel.CurrentResponse

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(weather: WeatherResponse)

    @Query("SELECT * FROM weatherData")
    fun getAllWeathers(): WeatherResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrent(response: CurrentResponse)

    @Query("SELECT * FROM currentData")
    fun getAllCurrent(): CurrentResponse


    @Query("SELECT * FROM weatherData")
    fun getAllListWeatherData(): List<WeatherResponse>

    @Query("SELECT * FROM currentData")
    fun getAllCurrentListData(): List<CurrentResponse>


}