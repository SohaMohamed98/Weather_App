package com.soha.weather_app.db.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.soha.weather_app.weather.db.models.weatherModel.FavouriteData
import com.soha.weather_app.weather.db.models.weatherModel.WeatherResponse
import com.soha.weather_app.weather.db.models.currentModel.CurrentResponse
import com.soha.weather_app.weather.db.models.currentModel.FavCurrent

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavWeather(fav : FavouriteData)

    @Query("SELECT * FROM favouriteData")
    fun getFavWether(): FavouriteData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavCurrent(response: FavCurrent)

    @Query("SELECT * FROM currentFavData")
    fun getFavCurrent():FavCurrent
}