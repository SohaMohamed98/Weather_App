package com.soha.weather_app.weather.view.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.soha.weather_app.R

import java.util.*

class DialogActivity : AppCompatActivity() {

    private lateinit var ringtone: Ringtone
    var event = ""
    var desc = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(applicationContext, notification)
        if (intent.extras != null) {
           // senderName = intent.getStringExtra("sender_name").toString()
            event = intent.getStringExtra("event").toString()
            desc = intent.getStringExtra("desc").toString()
            showDialogAlart(event, desc)
        }

    }


    @SuppressLint("SetTextI18n")
    private fun showDialogAlart(events:String, descs:String) {
        ringtone.play()
        val dialog = Dialog(this, R.style.ThemeOverlay_AppCompat_Dialog_Alert)
        dialog.setContentView(R.layout.dialog_custom_alarm)
        val txtEvent = dialog.findViewById<TextView>(R.id.dialog_event)
        val txtDesc = dialog.findViewById<TextView>(R.id.dialog_desc)
        val imgClose = dialog.findViewById<ImageView>(R.id.img_close)

        imgClose.setOnClickListener {
            if (ringtone.isPlaying) {
                ringtone.stop()
            }
            dialog.dismiss()
            finish()
        }


            txtEvent.visibility = View.VISIBLE
            txtDesc.visibility = View.VISIBLE
            txtEvent.text = events
            txtDesc.text = descs


        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Objects.requireNonNull(dialog.window)!!
                .setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        }
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}