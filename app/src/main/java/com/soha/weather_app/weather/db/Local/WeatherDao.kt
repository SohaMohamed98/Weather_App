package com.soha.weather_app.weather.db.Local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.soha.weather_app.weather.db.entity.AlarmEntity
import com.soha.weather_app.weather.db.entity.FavouriteData
import com.soha.weather_app.weather.db.entity.WeatherResponse

@Dao
interface WeatherDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(weather: WeatherResponse)

    @Query("SELECT * FROM weatherData")
    fun getAllWeathers(): WeatherResponse


    ////////////////////////////////////////////////////////////
    //Favourite
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavWeatherData(fav : FavouriteData)

    @Query("SELECT * FROM favouriteData")
    fun getFavWetherData(): List<FavouriteData>

    @Query("select * from favouriteData where id in (:num)")
   fun getDataById(num: Int): FavouriteData

    @Delete
    fun deleteFav(Fav: FavouriteData)

   /////////////////////////////////////////////////////////////



//=====================================================================
    //Alarm
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alertDatabase: AlarmEntity)

    @Query("select * from alarm_table")
    fun getAlarm(): LiveData<MutableList<AlarmEntity>>

    @Delete
    fun deleteAlarm(alertDatabase: AlarmEntity)

}