package com.soha.weather_app.weather.db.Local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.soha.weather_app.weather.db.entity.AlertEntity
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

    @Delete
    fun deleteFav(Fav: FavouriteData)

   /////////////////////////////////////////////////////////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)

    suspend fun insertAlert(alertDatabase: AlertEntity)

    @Query("select * from alert_table")
    fun getAlerts(): LiveData<MutableList<AlertEntity>>

    @Delete
     fun deleteAlert(alertDatabase: AlertEntity)



}