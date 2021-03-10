package com.soha.weather_app.db.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.soha.weather_app.weather.db.entity.FavouriteData
import com.soha.weather_app.weather.db.entity.WeatherResponse

@Dao
interface WeatherDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(weather: WeatherResponse)

    @Query("SELECT * FROM weatherData")
    fun getAllWeathers(): WeatherResponse


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavWeatherData(fav : FavouriteData)

    @Query("SELECT * FROM favouriteData")
    fun getFavWetherData(): List<FavouriteData>

    @Query("select * from favouriteData where id in (:num)")
   fun getDataById(num: Int): FavouriteData


}