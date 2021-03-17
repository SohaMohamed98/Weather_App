package com.soha.weather_app.weather.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.soha.weather_app.weather.view.activities.DialogActivity

class DialogReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val i = Intent(context, DialogActivity::class.java)
        i.putExtra("main", intent.getStringExtra("main"))
       // i.putExtra("desc", intent.getStringExtra("desc"))
       i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)

    }
}