package com.soha.weather_app.weather.view.fragments.setting.MapFragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.soha.weather_app.R
import com.soha.weather_app.databinding.FragmentSettingWeatherBinding
import com.soha.weather_app.utils.getAddressGeocoder
import com.soha.weather_app.utils.setLocale
import com.soha.weather_app.weather.db.Resource
import com.soha.weather_app.weather.viewModel.WeatherViewModel
import com.soha.weather_app.weather.viewModel.FavViewModel
import com.soha.weather_app.weather.viewModel.LocationViewModel


class SettingWeather : Fragment(R.layout.fragment_setting_weather) {

    lateinit var binding: FragmentSettingWeatherBinding
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var weatherViewModel: WeatherViewModel
    lateinit var favViewModel: FavViewModel

    lateinit var model: LocationViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ):
            View? {

        weatherViewModel =  ViewModelProvider(this).get(WeatherViewModel::class.java)
        favViewModel =  ViewModelProvider(this).get(FavViewModel::class.java)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        model = ViewModelProvider(requireActivity()).get(LocationViewModel::class.java)

        binding = FragmentSettingWeatherBinding.inflate(layoutInflater)

        model.getAddressData().observe(viewLifecycleOwner, Observer {
            binding.tvAddressLocation.text = it.toString()
        })

        model.getLatData().observe(viewLifecycleOwner, Observer {
            binding.tvLat.text = Math.round(it).toString()
        })

        model.getLonData().observe(viewLifecycleOwner, Observer {
            binding.tvLon.text = Math.round(it).toString()
        })

        binding = FragmentSettingWeatherBinding.inflate(layoutInflater)
        val root = binding.root

        binding.groubLocation.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.btnGps) {
                    getLastKnownLocation()
            } else if (checkedId == R.id.btnLocation) {
                loadFragment(MapsFragment())
            }
        }

        binding.groubTemp.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.btnCelicious) {
                model.setTempDegree("C")
                model.setTempData("metric")
            } else if (checkedId == R.id.btnKelven) {
                model.setTempDegree("K")
                model.setTempData("imperial")
            }
        }

        binding.groubWind.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.btnMs) {
                model.setWindDegree("M/S")
                model.setWindData("metric")

            } else if (checkedId == R.id.btnKmh) {
                model.setWindDegree("Km/h")
                model.setWindData("imperial")

            }
        }

        binding.groubLang.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId== R.id.btnEnglish){
                model.setLanguageData("en")
               // model.localeLang("en")
                setLocale(context as Activity, "en")

            } else if(checkedId == R.id.btnArabic){
                model.setLanguageData("ar")
               // model.localeLang("ar")
                setLocale(context as Activity, "en")
            }
        }

        binding.fbtnLocation.setOnClickListener {
            if (binding.tvLat.text == null || binding.tvLon.text == null) {
                Toast.makeText(context, "dddddf", Toast.LENGTH_LONG).show()
            } else {

                context?.let {
                    weatherViewModel.getWeatherAPIData(it,
                        model.getLatData().value.toString(),
                        model.getLonData().value.toString(),
                        model.getTempData().value.toString(),
                        model.getLanguageData().value.toString())
                }
                weatherViewModel.weatherLiveData.observe(viewLifecycleOwner, Observer {
                    when (it) {
                        is Resource.Success -> {

                            it.data?.let {

                            }
                        }
                        is Resource.Loading -> {
                            // showProgressBar()
                        }
                        is Resource.Error -> {
                            Toast.makeText(context, "errrrrrrrrrr", Toast.LENGTH_LONG).show()
                            //showErrorMessage(it.message)
                        }
                    }
                })

                context?.let {
                    favViewModel.getFavAPIData(it,
                        model.getLatData().value.toString(),
                        model.getLonData().value.toString(),
                        model.getTempData().value.toString(),
                        model.getLanguageData().value.toString())
                }
                favViewModel.favLiveData.observe(viewLifecycleOwner, Observer {
                    when (it) {
                        is Resource.Success -> {

                            it.data?.let {

                            }
                        }
                        is Resource.Loading -> {
                            // showProgressBar()
                        }
                        is Resource.Error -> {
                            Toast.makeText(context, "errrrrrrrrrr", Toast.LENGTH_LONG).show()
                            //showErrorMessage(it.message)
                        }
                    }
                })

            }

        }

        return root
    }

    private fun loadFragment(fragment: Fragment) {
        val fm = fragmentManager
        val fragmentTransaction: FragmentTransaction = fm!!.beginTransaction()
        fragmentTransaction.replace(R.id.relative, fragment)
        fragmentTransaction.commit() // save the changes
    }


    fun getLastKnownLocation() {
        if (context?.let {
                ActivityCompat.checkSelfPermission(it,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                model.setLatData(location.latitude)
                model.setLonData(location.longitude)
                val title= getAddressGeocoder(location.latitude, location.longitude, context)
                model.setAddressData(title)
                Toast.makeText(context, "${location.latitude}", Toast.LENGTH_LONG).show()
            }
        }

    }

}