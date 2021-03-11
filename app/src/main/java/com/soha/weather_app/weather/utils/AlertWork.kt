package com.soha.weather_app.weather.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.soha.weather_app.utils.formateTime
import com.soha.weather_app.weather.db.Repository
import com.soha.weather_app.weather.db.entity.WeatherResponse
import com.soha.weather_app.weather.db.model.Alert
import com.soha.weather_app.weather.receiver.AlertReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class AlertWork(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    private var weatherMutableLiveDataApi: MutableLiveData<WeatherResponse> = MutableLiveData()
    private var lat: String? = null
    private var lon: String? = null
    private var lang: String? = null
    private var units: String? = null
    private var windSpeed: String? = null
    private val mCtx = context
    private var requestCodeList = ArrayList<Int>()
    private val gson = Gson()
    private lateinit var alarmManager: AlarmManager
    private lateinit var alerts: List<Alert>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor


    override fun doWork(): Result {
        lat = inputData.getString("lat")
        lon = inputData.getString("lon")
        lang = inputData.getString("lang")
        units = inputData.getString("units")
        alerts = ArrayList()
        init()
        fetchWeather()
        return Result.success()
    }

    fun init() {
        alarmManager = mCtx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        sharedPreferences = mCtx.getSharedPreferences(
            "prefs",
            Context.MODE_PRIVATE
        )
        editor = sharedPreferences.edit()

    }

    @SuppressLint("SimpleDateFormat")
    fun setAlarm(alerts: List<Alert>) {
        Log.v("alertTest", alerts.size.toString() + " hello")
        val sdf = java.text.SimpleDateFormat("EEE, h:mm a")
        if (alerts.size > 0) {
            Log.v("wm", "2")
            for (alertItem in alerts) {
                Log.v("alertTest", "3")
                val now = System.currentTimeMillis()
                if (alertItem.start!! > now / 1000) {
                    Log.v("event", alertItem.event!!)
                    setNotification(
                        alertItem.start,
                        alertItem.event,
                        "From ${formateTime(alertItem.start!!)} " +
                                "to ${formateTime(alertItem.end!!)}"
                    )
                    Log.v("alertTest", "set alarm")
                } else if (alertItem.end!! > now / 1000) {
                    Log.v("alertTest", "set alarm")
                    Log.v("time", ((alertItem.start + alertItem.end) / 2).toString())
                    setNotification(
                        alertItem.end,
                        alertItem.event!!,
                        "From ${formateTime(alertItem.start)} " +
                                "to ${formateTime(alertItem.end)}"
                    )

                }

            }
            val requestCodeJson = gson.toJson(requestCodeList)
            editor.putString("requestsOfAlerts", requestCodeJson)
            editor.commit()
            editor.apply()
        }else{
            Log.v("alertTest", "no set alarm")

        }
    }

    fun fetchWeather(): MutableLiveData<WeatherResponse> {

        Log.v("alertTest",lat!!)
        Log.v("alertTest",lon!!)
        Log.v("alertTest",units!!)
        Log.v("alertTest",lang!!)
       GlobalScope.launch {
            Dispatchers.IO

            try {
                val repo=Repository()
                //"68.3963", "36.9419"
                val response = repo.retrofitWeatherCall(lat!!, lon!!, units!!, lang!!)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        weatherMutableLiveDataApi.value = response.body()
                       // Log.v("apiData",weatherMutableLiveDataApi.value.toString())
                        response.body()!!.alerts?.let {
                            setAlarm(it)

                            Log.v("alertTest", alerts.toString() + "hello")

                        }
                        setAlarm(ArrayList<Alert>())
                    }
                }

            } catch (e: Exception) {
                Log.i("testError11", " " + e.message)
            }
        }
        return weatherMutableLiveDataApi

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun setNotification(startTime: Int, event: String, description: String) {
        val intent = Intent(mCtx, AlertReceiver::class.java)
        intent.putExtra("event", event)
        intent.putExtra("desc", description)
        val r = Random()
        val i1 = r.nextInt(99)

        val pendingIntent = PendingIntent.getBroadcast(mCtx, i1, intent, 0)
        requestCodeList.add(i1)
        val alertTime: Long = startTime.toLong()
        Log.v("alertTime", alertTime.toString())

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alertTime, pendingIntent)
        // Toast.makeText(mCtx, R.string.set_alarm, Toast.LENGTH_LONG).show()
        mCtx.registerReceiver(AlertReceiver(), IntentFilter())
    }
}