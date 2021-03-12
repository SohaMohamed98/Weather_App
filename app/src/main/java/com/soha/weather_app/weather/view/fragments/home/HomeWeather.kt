package com.soha.weather_app.weather.view.fragments.home


import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.soha.weather_app.R
import com.soha.weather_app.databinding.FragmentHomeWeatherBinding
import com.soha.weather_app.weather.db.Resource
import com.soha.weather_app.weather.db.model.Hourly
import com.soha.weather_app.weather.db.entity.WeatherResponse
import com.soha.weather_app.weather.receiver.AlertReceiver
import com.soha.weather_app.weather.utils.AlertWork
import com.soha.weather_app.weather.utils.dayConverter
import com.soha.weather_app.weather.utils.setImage
import com.soha.weather_app.weather.utils.timeConverter
import com.soha.weather_app.weather.view.adapters.HourlyAdapter
import com.soha.weather_app.weather.viewModel.SettingViewModel
import com.soha.weather_app.weather.viewModel.WeatherViewModel
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit


class HomeWeather : Fragment(R.layout.fragment_home_weather) {
    lateinit var binding: FragmentHomeWeatherBinding
    lateinit var sp: SharedPreferences
    private var hourAdapter: RecyclerView.Adapter<HourlyAdapter.ForecatViewHolder>? = null
    private var layoutManag: RecyclerView.LayoutManager? = null
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var workManager: WorkManager
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        workManager = WorkManager.getInstance(requireContext())

        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)

        binding = FragmentHomeWeatherBinding.inflate(layoutInflater)
        val root = binding.root
        binding.recyclerViewCurrent.isEnabled = false
        context?.let {
            weatherViewModel.getWeatherAPIData(it)
        }

        weatherViewModel.weatherLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    //binding.cardHello.visibility = View.GONE
                    binding.mainContainer.visibility = View.VISIBLE
                    hideProgressBar()
                    it.data?.let {
                    }
                }
                is Resource.Loading -> {
                  //  binding.cardHello.visibility = View.VISIBLE
                    showProgressBar()
                }
                is Resource.Error -> {
                    showErrorMessage(it.message)
                }
            }
        })

        weatherViewModel.checkRoom.observe(viewLifecycleOwner, Observer {
            if (it == true){
                binding.cardHello.visibility = View.VISIBLE
            }
        })

        weatherViewModel.weatherFromRoomLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                   // binding.cardHello.visibility = View.GONE
                    binding.mainContainer.visibility = View.VISIBLE
                    it.data?.let {
                        displayDailyWeatherToRecycleView(it.hourly)
                        displayCurrentWeatherToCard(it)
                    }
                }
                is Resource.Loading -> {
                }
                is Resource.Error -> {
                    showErrorMessage(it.message)
                }
            }
        })

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = sharedPreferences.edit()
        sp = PreferenceManager.getDefaultSharedPreferences(context)
        binding.tvAddress.text = sp.getString("address", "")
        binding.textCelcius.text = sp.getString("cel", "")




        setUpAlerts()

        return root
    }

    private fun displayCurrentWeatherToCard(it1: WeatherResponse) {

        binding.tvTemp.text = Math.round(it1.daily.get(0).temp.day).toString()
        binding.tvTempMax.text = Math.round(it1.daily.get(0).temp.max).toString()
        binding.tvTempMin.text = Math.round(it1.daily.get(0).temp.min).toString()
        binding.tvHumidity.text = Math.round(it1.current.humidity).toString()
        binding.tvUpdatedAt.text = dayConverter(it1.current.dt.toLong())
        binding.tvStatus.text = it1.current.weather.get(0).description
        binding.tvWind.text = Math.round(it1.current.windSpeed).toString()
        binding.tvPressure.text = Math.round(it1.current.pressure).toString()
        binding.tvSunrise.text = timeConverter(it1.current.sunrise.toLong())
        binding.tvSunset.text = timeConverter(it1.current.sunset.toLong())
        binding.tvVis.text = it1.current.visibility.toString()//mm
        val url = it1.current.weather.get(0).icon

        setImage(binding.imgCurrentItem, url)

    }


    @SuppressLint("WrongConstant")
    private fun initUI(data: List<Hourly>) {
        context?.let {
            var hourlyAd = HourlyAdapter(data)
            hourlyAd.setData(data, it)
            binding.recyclerViewCurrent.apply {
                layoutManag = LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false)
                layoutManager = layoutManag
                hourAdapter = hourlyAd
                adapter = hourAdapter
            }
        }

    }

    fun displayDailyWeatherToRecycleView(data: List<Hourly>) {
        if (data != null) {
            initUI(data)
        }
    }

    private fun showErrorMessage(message: String?) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show()
        System.out.println("Error is: " + message)
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
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

    fun cancelAlarm(requestCode: Int) {
        val intent = Intent(requireContext(), AlertReceiver::class.java)
        val sender = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }

    private fun setUpFetchFromApiWorker() {
        val data: Data = Data.Builder().putString("lat", sp.getString("lat", "")).putString(
            "lon",
            sp.getString("lon", "")
        ).putString("lang",  sp.getString("lang", "")).putString("units",  sp.getString("temp", ""))
            .build()
        val constrains = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val repeatingRequest = PeriodicWorkRequest.Builder(
            AlertWork::class.java, 1,
            TimeUnit.HOURS
        )
            .addTag("PeriodicWork")
            .setConstraints(constrains)
            .setInputData(data)
            .build()
        workManager.enqueue(repeatingRequest)
        workManager.getWorkInfoByIdLiveData(repeatingRequest.id).observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                Log.v("state", it.state.name)
            })
    }



}

