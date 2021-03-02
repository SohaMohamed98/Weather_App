package com.soha.weather_app.db.Local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.soha.weather_app.utils.model.WeatherResponse
import com.soha.weather_app.weather.db.Local.ConverterCurrent
import com.soha.weather_app.weather.db.model.currentModel.CurrentResponse

@Database(
    entities = [WeatherResponse::class],
    version = 1)
@TypeConverters(TypeConvertDataBase::class, ConverterCurrent::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao

  /*  companion object {
        @Volatile
        private var instance: WeatherDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }
        private fun createDatabase(context: Context) =
                Room.databaseBuilder(
                        context.applicationContext,
                        WeatherDatabase::class.java,
                        "weather_db").build()

    }*/

    companion object {
        @Volatile
        private var instance: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {

            return instance ?: synchronized(this) {
                instance ?: createDatabase(context).also { instance = it }
            }

        }

        /*companion object{
            @Volatile var db: WeatherDatabase? = null
            fun getInstance(application: Context): WeatherDatabase?{
                if(db ==null){
                    db =Room.databaseBuilder(application.applicationContext,
                            WeatherDatabase::class.java,"day_db").allowMainThreadQueries().build()
                }
                return db
            }
        }*/

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
                "my_weather").build()

    }

}