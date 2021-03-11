package com.soha.weather_app.weather.db.Local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.soha.weather_app.weather.db.entity.AlertEntity
@Dao

@TypeConverters(TypeConverters::class)
interface AlerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)

    suspend fun insertAlert(alertDatabase: AlertEntity)

    @Query("select * from alert_table")
    fun getAlerts(): LiveData<MutableList<AlertEntity>>

    @Delete
    suspend fun deleteAlert(alertDatabase: AlertEntity)

}