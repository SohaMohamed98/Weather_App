package com.soha.weather_app.db.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.soha.weatherapp.model.WeatherResponse

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(weather: WeatherResponse)

    @Query("SELECT * FROM weatherInfo")
    fun getAllWeathers(): WeatherResponse


}