package com.soha.weather_app.db.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.soha.weather_app.utils.model.WeatherResponse
import com.soha.weather_app.weather.db.model.currentModel.CurrentResponse

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(weather: WeatherResponse)

    @Query("SELECT * FROM weatherData")
    fun getAllWeathers(): WeatherResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrent(response:CurrentResponse)

    @Query("SELECT * FROM weatherData")
    fun getAllCurrent(): CurrentResponse


}