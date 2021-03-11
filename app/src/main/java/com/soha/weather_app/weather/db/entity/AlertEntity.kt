package com.soha.weather_app.weather.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "alert_table")
data class AlertEntity(
    @ColumnInfo
    val requestCode:Int,
    val event: String,
    val start: String,
    val description: String,
    val status:Boolean
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}