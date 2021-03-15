package com.soha.weather_app.weather.provider

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.soha.weather_app.weather.db.Repository
import com.soha.weather_app.weather.db.entity.WeatherResponse
import com.soha.weather_app.weather.db.model.Alert
import com.soha.weather_app.weather.db.model.Current
import com.soha.weather_app.weather.receiver.AlertReceiver
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class AlertBuild(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    private var weatherMutableLiveDataApi: MutableLiveData<WeatherResponse> = MutableLiveData()
    private var lat: String? = null
    private var lon: String? = null
    private var lang: String? = null
    private var units: String? = null
    private val context = context
    private var requestCodeList = ArrayList<Int>()
    private val gson = Gson()
    private lateinit var alarmManager: AlarmManager
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    init {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    override fun doWork(): Result {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        lat = sharedPreferences.getString("lat", "")
        lon = sharedPreferences.getString("lon", "")
        units = sharedPreferences.getString("temp", "")
        lang = sharedPreferences.getString("lang", "")

        getWeatherData()
        return Result.success()
    }


    fun getWeatherData(): MutableLiveData<WeatherResponse> {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = Repository()
                val response =
                    repo.retrofitWeatherCall(lat.toString(), lon.toString(), units!!, lang!!)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        weatherMutableLiveDataApi.value = response.body()
                        response.body()!!.alerts?.let {
                            setAlarm(it)
                            response.body()!!.current?.let {
                                setCustomAlarm(it)
                            }
                        }
                        setAlarm(ArrayList<Alert>())
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return weatherMutableLiveDataApi

    }


    @SuppressLint("SimpleDateFormat")
    fun setAlarm(alerts: List<Alert>) {
        if (alerts.size > 0) {
            for (alertItem in alerts) {
                val now = System.currentTimeMillis()
                if (alertItem.start!! > now / 1000) {
                    setNotification(alertItem.start, alertItem.event!!, alertItem.description!!)
                } else if (alertItem.end!! > now / 1000) {
                    setNotification(alertItem.end, alertItem.event!!, alertItem.description!!)
                }

            }
            val requestCodeJson = gson.toJson(requestCodeList)
            editor.putString("requestsOfAlerts", requestCodeJson)
            editor.commit()
            editor.apply()
        } else {
            Toast.makeText(context, "no set alarm", Toast.LENGTH_SHORT).show()

        }
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun setNotification(startTime: Int, event: String, description: String) {

        val intent = Intent(context, AlertReceiver::class.java)
        intent.putExtra("event", event)
        intent.putExtra("desc", description)
        val requestCode = Random().nextInt(99)
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        requestCodeList.add(requestCode)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTime.toLong(), pendingIntent)
        context.registerReceiver(AlertReceiver(), IntentFilter())
    }


    fun setCustomNotification(startTime: Int, main: String) {
        val intent = Intent(context, AlertReceiver::class.java)
        intent.putExtra("main", main)
       // intent.putExtra("description", description)
        val r = Random()
        val i1 = r.nextInt(99)

        val pendingIntent = PendingIntent.getBroadcast(context, i1, intent, 0)
        requestCodeList.add(i1)
        val alertTime: Long = startTime.toLong()

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alertTime, pendingIntent)
        context.registerReceiver(AlertReceiver(), IntentFilter())
    }

    @SuppressLint("SimpleDateFormat")
    fun setCustomAlarm(weather: Current) {
        if (weather != null) {
            val now = System.currentTimeMillis()
            if (weather.dt!! > now / 1000) {
                setCustomNotification(weather.dt,
                    weather.weather.get(0).main!!)
            } else if (weather.dt!! > now / 1000) {

                setCustomNotification(weather.dt, weather.weather.get(0).main!!)

            }


            val requestCodeJson = gson.toJson(requestCodeList)
            editor.putString("requestsOfWeather", requestCodeJson)
            editor.commit()
            editor.apply()
        } else {
            Toast.makeText(context, "no set weather", Toast.LENGTH_SHORT).show()

        }
    }

}