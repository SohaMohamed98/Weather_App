package com.soha.weather_app.weather.db.Local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.soha.weather_app.weather.db.entity.AlertEntity
import com.soha.weather_app.weather.db.entity.CustomAlert
import com.soha.weather_app.weather.db.entity.FavouriteData
import com.soha.weather_app.weather.db.entity.WeatherResponse

@Database(
    entities = [WeatherResponse::class, FavouriteData::class, AlertEntity::class, CustomAlert::class],
    version = 5)
@TypeConverters(TypeConvertDataBase::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao

    companion object {
        @Volatile
        private var instance: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {

            return instance ?: synchronized(this) {
                instance ?: createDatabase(context).also { instance = it }
            }

        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
                "my_weather").build()

    }

}