package com.soha.weather_app.weather.view.activities

import android.app.*
import android.content.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soha.alert.viewModel.AlertAdapter
import com.soha.alert.viewModel.AlertsViewModel
import com.soha.weather_app.R
import com.soha.weather_app.databinding.FragmentAlertsBinding
import com.soha.weather_app.weather.db.entity.AlertEntity
import com.soha.weather_app.weather.db.model.Alert
import com.soha.weather_app.weather.provider.Setting
import com.soha.weather_app.weather.receiver.AlertReceiver
import com.soha.weather_app.weather.receiver.DialogReceiver
import com.soha.weather_app.weather.utils.formateTime
import com.soha.weather_app.weather.viewModel.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AlertsFragment : Fragment(R.layout.fragment_alerts) {
    var myHour: Int? = null
    var myMin: Int? = null
    var myYear: Int? = null
    var myMon: Int? = null
    var myDay: Int? = null
    private lateinit var binding: FragmentAlertsBinding
    private lateinit var alarmManager: AlarmManager
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var viewModel: WeatherViewModel
    private lateinit var alertViewModel: AlertsViewModel
    private lateinit var alertAdapter: AlertAdapter
    private lateinit var alertList: List<Alert>
    private var notificationOrAlarm = "notification"
    lateinit var prefs: SharedPreferences


    /* private fun loadSettings() {
        val unit_system = sharedPreferences.getString("UNIT_SYSTEM", "")
        val language_system = sharedPreferences.getString("LANGUAGE_SYSTEM", "")
        val device_Location = sharedPreferences.getBoolean("USE_DEVICE_LOCATION", false)
        val notifications = sharedPreferences.getBoolean("USE_NOTIFICATIONS_ALERT", false)
        val custom_Locations = sharedPreferences.getString("CUSTOM_LOCATION", "")
        val wind_speed = sharedPreferences.getString("WIND_SPEED", "")
        val mapLocation = sharedPreferences.getBoolean("MAP_LOCATION", false)


        if (unit_system != null) {
            Setting.unitSystem = unit_system
        }
        if (language_system != null) {
            Setting.languageSystem = language_system
        }

        if (device_Location != null) {
            Setting.deviceLocation = device_Location
        }

        if (notifications != null) {
            Setting.notifications = notifications
        }

        if (custom_Locations != null) {
            Setting.customLocations = custom_Locations
        }
//        if (wind_speed != null) {
//            Settings. = wind_speed
//        }
        Setting.mapLocation = mapLocation!!
    }*/

    private fun init() {
        sharedPreferences = requireActivity().getSharedPreferences(
            "prefs", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        alarmManager =
            requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alertList = ArrayList()
        binding.alertRV.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.alertRV.setHasFixedSize(true)
        alertAdapter = AlertAdapter(requireContext())
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView( binding.alertRV)
        alertViewModel = ViewModelProvider(this).get(AlertsViewModel::class.java)

       viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding= FragmentAlertsBinding.inflate(layoutInflater)
      //  binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alerts, container, false)
        binding.alertDate.setOnClickListener {
            getDate()
        }
        binding.alertTime.setOnClickListener {
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
        //loadSettings()

        getAlertFromDB()
        binding.btnAdd.setOnClickListener {
            binding.alertTime.text = " "
            binding.alertDate.text = " "
            if (myHour != null && myMin != null && myDay != null && myMon != null && myYear != null) {
                val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")

                val date: String =
                    myDay.toString() + "-" + myMon + "-" + myYear + " " + myHour + ":" + myMin
                val dateLong = sdf.parse(date)!!.time
                Log.v("datelong", dateLong.toString())
                if (alertList.size > 0) {
                    for (alertItem in alertList) {
                        if (dateLong / 1000 > alertItem.start!! && dateLong / 1000 < alertItem.end!!) {
                            if (notificationOrAlarm.equals("notification")) {
                                setNotification(
                                    myHour!!,
                                    myMin!!,
                                    myDay!!,
                                    myMon!!,
                                    myYear!!,
                                    alertItem.event!!,
                                    "From ${formateTime(alertItem.start)} to ${formateTime(alertItem.end)}"
                                )
                                Log.v("gg", "ggggg")

                            } else {
                                setAlaram(
                                    alertItem.event!!,
                                    alertItem.description!!, myHour!!,
                                    myMin!!,
                                    myDay!!,
                                    myMon!!,
                                    myYear!!
                                )
                                Log.v("gg", "gggg2g")
                            }
                            break

                        }
                    }
                }else{
                    if (notificationOrAlarm.equals("notification")) {
                        setNotification(
                            myHour!!,
                            myMin!!,
                            myDay!!,
                            myMon!!,
                            myYear!!,
                            "Nothing",
                            "No Dangerous Alert"
                        )
                        Log.v("gg","1")
                    } else {
                        setAlaram(
                            "Nothing",
                            "No Dangerous Alert", myHour!!,
                            myMin!!,
                            myDay!!,
                            myMon!!,
                            myYear!!
                        )
                        Log.v("gg","2")
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "Data is empty", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun setNotification(
        hour: Int,
        min: Int,
        day: Int,
        month: Int,
        year: Int,
        event: String,
        description: String
    ) {
        val intentA = Intent(context, AlertReceiver::class.java)
        intentA.putExtra("event", event)
        intentA.putExtra("desc", description)
        val r = Random()
        val i1 = r.nextInt(99)
        val pendingIntentA = PendingIntent.getBroadcast(context, i1, intentA, 0)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, min)
        calendar[Calendar.MONTH] = month - 1
        calendar[Calendar.DATE] = day
        calendar[Calendar.YEAR] = year
        calendar[Calendar.SECOND] = 0
        val alarmtime: Long = calendar.timeInMillis
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmtime, pendingIntentA)
        Toast.makeText(context,"Done!", Toast.LENGTH_LONG).show()
        requireActivity().registerReceiver(AlertReceiver(), IntentFilter())
        var date = day.toString() + "/" + month + "/" + year + " " + hour + ":" + min

        addAlert(i1, event, date, description, true)
    }


    private fun getAlertFromDB() {
        alertViewModel.getAlert(requireContext()).observe(viewLifecycleOwner,  {
            it?.let {
                alertAdapter.fetchData(it, requireContext())
                binding.alertRV.adapter = alertAdapter
            }

        })

    }



    private fun addAlert(requestCode: Int, event: String, start: String,
                         description: String, status: Boolean) {
        val alert = AlertEntity(requestCode, event, start, description, status)
        GlobalScope.launch {
            Dispatchers.IO
            alertViewModel.addAlert(alert,requireContext())
        }


    }


    private fun setAlaram(event: String, desc: String,
                          hour: Int, min: Int,
                          day: Int, month: Int, year: Int
    ) {
        val intentA = Intent(context, DialogReceiver::class.java)
        intentA.putExtra("event", event)
        intentA.putExtra("desc", desc)
        val r = Random()
        val i1 = r.nextInt(99)
        val pendingIntentA = PendingIntent.getBroadcast(context, i1, intentA, 0)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, min)
        calendar[Calendar.MONTH] = month - 1
        calendar[Calendar.DATE] = day
        calendar[Calendar.YEAR] = year
        calendar[Calendar.SECOND] = 0
        val alarmtime: Long = calendar.timeInMillis
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmtime, pendingIntentA)
        Toast.makeText(context,"Done!", Toast.LENGTH_LONG).show()
        requireActivity().registerReceiver(DialogReceiver(), IntentFilter())
        var date = day.toString() + "/" + month + "/" + year + " " + hour + ":" + min

        addAlert(i1, event, date, desc, true)
    }



    private fun getDate() {
        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(requireContext(), { view, year, monthOfYear, dayOfMonth ->
                binding.alertDate.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
                binding.alertDate.visibility = View.VISIBLE

                myMon = monthOfYear + 1
                myYear = year
                myDay = dayOfMonth
            }, year, month, day
        )
        dpd.datePicker.minDate = System.currentTimeMillis() - 1000
        dpd.show()
    }



    private fun getTime() {
        val c = Calendar.getInstance()
        var hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)
        val datetime = Calendar.getInstance()

        val tpd = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                c[Calendar.HOUR_OF_DAY] = h
                c[Calendar.MINUTE] = m
                if (c.timeInMillis >= datetime.timeInMillis) {
                    binding.alertTime.setText("" + h + ":" + m)
                    binding.alertTime.visibility = View.VISIBLE

                    myHour = h
                    myMin = m
                    Log.v("alert", "" + myHour + ":" + myMin)
                } else {
                    Toast.makeText(requireActivity(), "Invalide Data", Toast.LENGTH_LONG).show()
                    binding.alertTime.setText(" ")
                    binding.alertTime.visibility = View.VISIBLE
                }
            }), hour, minute, false
        )

        tpd.show()
    }



    // swipe for delete
    var itemTouchHelper: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                AlertDialog.Builder(activity).setMessage("Do You Want to Delete this Alert ?!")
                    .setPositiveButton("Yes",
                        DialogInterface.OnClickListener { dialog, id -> //when delete item from tripDatabase, add tripHistoryDB
                            val alertItemDeleted = alertAdapter.getItemByVH(viewHolder)
                            cancelAlarm(alertItemDeleted.requestCode)
                            CoroutineScope(Dispatchers.IO).launch {
                                deleteFavoriteItemFromDB(alertItemDeleted)
                            }
                            alertAdapter.removeAlertItem(viewHolder)
                        })
                    .setNegativeButton("No",
                        DialogInterface.OnClickListener { dialog, id ->
                            getAlertFromDB()
                        }).show()

            }
        }


    // delete favorite item
    suspend fun deleteFavoriteItemFromDB(alertDB: AlertEntity) {
        alertViewModel.deleteAlert(alertDB,requireContext())
    }


    fun cancelAlarm(requestCode: Int) {
        val intent = Intent(requireContext(), AlertReceiver::class.java)
        val sender = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }
    }