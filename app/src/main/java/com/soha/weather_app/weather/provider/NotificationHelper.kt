package com.soha.weather_app.weather.provider

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.soha.weather_app.R

class NotificationHelper(base: Context?, intent: Intent) : ContextWrapper(base) {
    private var mManager: NotificationManager? = null
    private lateinit var intent: Intent

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(
            channelID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.enableLights(true)
        channel.setLightColor(Color.RED)
        channel.enableVibration(true)
        manager!!.createNotificationChannel(channel)
    }
    val manager: NotificationManager?
        get() {
            if (mManager == null) {
                mManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return mManager
        }

    val channelNotification: NotificationCompat.Builder
        get() = NotificationCompat.Builder(
            applicationContext,
            channelID
        )
            .setContentTitle("Alert")
            .setContentText(intent.getStringExtra("event") + " " + intent.getStringExtra("desc"))
            .setSmallIcon(R.drawable.ic_launcher_foreground)

    companion object {
        const val channelID = "channelID"
        const val channelName = "Channel Name"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.intent = intent
            createChannel()
        }
    }
}