package com.soha.weather_app.weather.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.soha.weather_app.weather.provider.Notification

class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val notification = Notification(context,intent)  //go to notification with event & desc
        val builder: NotificationCompat.Builder = notification.channelNotification
        notification.manager?.notify(1, builder.build())

    }
}