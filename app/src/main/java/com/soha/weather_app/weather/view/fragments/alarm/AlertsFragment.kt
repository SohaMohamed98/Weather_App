package com.soha.weather_app.weather.view.fragments.alarm

import android.app.*
import android.content.*
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.soha.alert.viewModel.AlertsViewModel
import com.soha.weather_app.R
import com.soha.weather_app.databinding.FragmentAlertsBinding
import com.soha.weather_app.weather.db.entity.AlertEntity
import com.soha.weather_app.weather.db.model.Alert
import com.soha.weather_app.weather.provider.AlertBuild
import com.soha.weather_app.weather.receiver.AlertReceiver
import com.soha.weather_app.weather.receiver.DialogReceiver
import com.soha.weather_app.weather.viewModel.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AlertsFragment : Fragment(R.layout.fragment_alerts) {
    var myHour: Int? = 0
    var myMin: Int? = 0
    var myYear: Int? = 0
    var myMon: Int? = 0
    var myDay: Int? = 0
    lateinit var sp:SharedPreferences
    private lateinit var binding: FragmentAlertsBinding
    private lateinit var alarmManager: AlarmManager
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var alertViewModel: AlertsViewModel
    private lateinit var alertAdapter: AlertAdapter
    private lateinit var alertList: List<Alert>
    private var notificationOrAlarm = "notification"
    lateinit var prefs: SharedPreferences

    private lateinit var workManager: WorkManager



    private fun init() {
        sp = PreferenceManager.getDefaultSharedPreferences(context)
        workManager = WorkManager.getInstance(requireContext())
        sharedPreferences = requireActivity().getSharedPreferences(
            "prefs", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        alarmManager =
            requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alertList = ArrayList()
        //  layoutManag = LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false)
        binding.alertRV.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.alertRV.setHasFixedSize(true)
        alertAdapter = AlertAdapter(requireContext())
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.alertRV)
        alertViewModel = ViewModelProvider(this).get(AlertsViewModel::class.java)
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAlertsBinding.inflate(layoutInflater)
        binding.layoutDate.setOnClickListener {
            getDate()
        }
        binding.layoutTime.setOnClickListener {
            getTime()
        }
        binding.radioGroupNOrA.setOnCheckedChangeListener({ group, checkedId ->
            if (checkedId == R.id.notification) {
                notificationOrAlarm = "notification"
            } else {
                notificationOrAlarm = "alarm"
            }
        })
        init()

        getAlertFromDBToRecyclerView()


        binding.btnAdd.setOnClickListener {
            binding.tvTime.text = " "
            binding.tvDate.text = " "
            if (myHour != null && myMin != null && myDay != null && myMon != null && myYear != null) {
                val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")

                val date: String =
                    myDay.toString() + "-" + myMon + "-" + myYear + " " + myHour + ":" + myMin
                val dateLong = sdf.parse(date)!!.time
                if (alertList.size > 0) {
                    for (alertItem in alertList) {
                        if (dateLong / 1000 > alertItem.start!! && dateLong / 1000 < alertItem.end!!) {
                            if (notificationOrAlarm.equals("notification")) {
                                setNotification(myHour!!, myMin!!,
                                    myDay!!, myMon!!, myYear!!,
                                    alertItem.event!!, alertItem.description!!)
                            } else {
                                setAlaram(alertItem.event!!, alertItem.description!!,
                                    myHour!!, myMin!!,
                                    myDay!!, myMon!!, myYear!!
                                )
                            }
                            break
                        }
                    }
                } else {
                    if (notificationOrAlarm.equals("notification")) {
                        setNotification(myHour!!, myMin!!,
                            myDay!!, myMon!!, myYear!!,
                            "No event now.", "NOT Dangerous Weather!!")
                    } else {
                        setAlaram("NOT Event", "NOT Dangerous Weather!!",
                            myHour!!, myMin!!,
                            myDay!!, myMon!!, myYear!!)
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "Data is empty", Toast.LENGTH_LONG).show()
            }
        }

        setUpAlerts()
        return binding.root
    }


    private fun getAlertFromDBToRecyclerView() {
        alertViewModel.getAlert(requireContext()).observe(viewLifecycleOwner, {
            it?.let {
                alertAdapter.fetchData(it, requireContext())
                binding.alertRV.adapter = alertAdapter
            }

        })

    }


    private fun addAlert(requestCode: Int, event: String, start: String,
                         description: String, status: Boolean) {
        val alert = AlertEntity(requestCode, event, start, description, status)
        CoroutineScope(Dispatchers.IO).launch {
            alertViewModel.addAlert(alert, requireContext())
        }

    }

    private fun setAlaram(event: String, desc: String,
        hour: Int, min: Int,
        day: Int, month: Int, year: Int, ) {
        val intentDialogueReciever = Intent(context, DialogReceiver::class.java)
        intentDialogueReciever.putExtra("event", event)
        intentDialogueReciever.putExtra("desc", desc)
        val random = Random()
        val requestCode = random.nextInt(99)
        val pendingIntentDialogueReciever =
            PendingIntent.getBroadcast(context, requestCode, intentDialogueReciever, 0)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, min)
        calendar[Calendar.MONTH] = month - 1
        calendar[Calendar.DATE] = day
        calendar[Calendar.YEAR] = year
        calendar[Calendar.SECOND] = 0
        val alarmtime: Long = calendar.timeInMillis
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmtime, pendingIntentDialogueReciever)
        Toast.makeText(context, "Alert is Done!", Toast.LENGTH_LONG).show()
        requireActivity().registerReceiver(DialogReceiver(), IntentFilter())
        var date = day.toString() + "_" + month + "_" + year + " " + hour + ":" + min

        addAlert(requestCode, event, date, desc, true)
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun setNotification(
        hour: Int, min: Int,
        day: Int, month: Int, year: Int,
        event: String, description: String,
    ) {
        val intentAlertReciever = Intent(context, AlertReceiver::class.java)
        intentAlertReciever.putExtra("event", event)
        intentAlertReciever.putExtra("desc", description)
        val random = Random()
        val requestCode = random.nextInt(99)
        val pendingIntentAlertReciever =
            PendingIntent.getBroadcast(context, requestCode, intentAlertReciever, 0)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, min)
        calendar[Calendar.MONTH] = month - 1
        calendar[Calendar.DATE] = day
        calendar[Calendar.YEAR] = year
        calendar[Calendar.SECOND] = 0
        val alarmtime: Long = calendar.timeInMillis
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmtime, pendingIntentAlertReciever)
        Toast.makeText(context, "Alert is done!", Toast.LENGTH_LONG).show()
        requireActivity().registerReceiver(AlertReceiver(), IntentFilter())
        var date = day.toString() + "/" + month + "/" + year + " " + hour + ":" + min

        addAlert(requestCode, event, date, description, true)
    }


    private fun getDate() {
        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog =
            DatePickerDialog(requireContext(), { view, year, monthOfYear, dayOfMonth ->
                binding.tvDate.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
                binding.tvDate.visibility = View.VISIBLE

                myMon = monthOfYear + 1
                myYear = year
                myDay = dayOfMonth
            }, year, month, day
            )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }


    private fun getTime() {
        val c = Calendar.getInstance()
        var hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)
        val datetime = Calendar.getInstance()

        val timePickerDialog = TimePickerDialog(requireContext(),
            TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                //Hour_of_Day
                c[Calendar.HOUR] = h
                c[Calendar.MINUTE] = m
                if (c.timeInMillis >= datetime.timeInMillis) {
                    binding.tvTime.setText("" + h + ":" + m)
                    binding.tvTime.visibility = View.VISIBLE

                    myHour = h
                    myMin = m
                } else {
                    Toast.makeText(requireActivity(), "Invalide Date or Time", Toast.LENGTH_LONG)
                        .show()
                    binding.tvTime.setText(" ")
                    binding.tvTime.visibility = View.VISIBLE
                }
            }), hour, minute, false)

        timePickerDialog.show()
    }


    // swipe for delete
    var itemTouchHelper: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                AlertDialog.Builder(activity).setMessage("Do You Want to Delete this Alert ?!")
                    .setPositiveButton("Yes",
                        DialogInterface.OnClickListener { dialog, id ->
                            val alertItemDeleted = alertAdapter.getItemByVH(viewHolder)
                            cancelAlarm(alertItemDeleted.requestCode)
                            setUpAlerts()
                            CoroutineScope(Dispatchers.IO).launch {
                                deleteFavoriteItemFromDB(alertItemDeleted)
                            }
                            alertAdapter.removeAlertItem(viewHolder)
                        })
                    .setNegativeButton("No",
                        DialogInterface.OnClickListener { dialog, id ->
                            getAlertFromDBToRecyclerView()
                        }).show()

            }
        }


    // delete favorite item
    suspend fun deleteFavoriteItemFromDB(alertDB: AlertEntity) {
        alertViewModel.deleteAlert(alertDB, requireContext())
    }


    fun cancelAlarm(requestCode: Int) {
        val intent = Intent(requireContext(), AlertReceiver::class.java)
        val sender = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }

    private fun setUpAlerts() {
        if (sharedPreferences.getBoolean("ALERT", true) && sp.getString("alerts", "yes")
                .equals("yes")
        ) {
            setUpFetchFromApiWorker()
            editor.putString("alerts", "no")
            editor.commit()
            editor.apply()
        } else if (!sharedPreferences.getBoolean("ALERT", true)) {
            val requestCodeListJson = sp.getString("requestsOfAlerts", " ")
            val type: Type = object : TypeToken<List<Int>>() {}.type

            if (Gson().fromJson<List<Int>>(requestCodeListJson, type) != null) {
                var requestCodeList: List<Int> = Gson().fromJson(requestCodeListJson, type)
                for (requestCodeItem in requestCodeList) {
                    cancelAlarm(requestCodeItem)

                }
                workManager.cancelAllWorkByTag("PeriodicWork")
                editor.putString("alerts", "yes")
                editor.commit()
                editor.apply()
            }

        }
    }

    private fun setUpFetchFromApiWorker() {
        val data: Data = Data.Builder().putString("lat", sp.getString("lat", ""))
            .putString("lon", sp.getString("lon", ""))
            .putString("temp",  sp.getString("temp", ""))
            .putString("lang",  sp.getString("lang", ""))
            .build()
        val constrains = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val repeatingRequest = PeriodicWorkRequest.Builder(AlertBuild::class.java, 1, TimeUnit.HOURS)
            .addTag("PeriodicWork")
            .setConstraints(constrains)
            .setInputData(data)
            .build()
        workManager.enqueue(repeatingRequest)

    }
}