package com.soha.weather_app.weather.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.soha.alert.view.DialogActivity

class DialogReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val i = Intent(context, DialogActivity::class.java)
        i.putExtra("event", intent.getStringExtra("event"))
        i.putExtra("desc", intent.getStringExtra("desc"))
       i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)

    }
}