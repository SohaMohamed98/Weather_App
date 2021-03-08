package com.soha.weather_app.weather.fragments.setting

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.soha.weather_app.R
import com.soha.weather_app.databinding.FragmentHomeWeatherBinding
import com.soha.weather_app.databinding.FragmentSettingWeatherBinding
import com.soha.weather_app.weather.db.Repository
import com.soha.weather_app.weather.db.Resource
import com.soha.weather_app.weather.fragments.current.CurrentViewModel
import com.soha.weather_app.weather.fragments.current.HomeWeather
import com.soha.weather_app.weather.fragments.current.WeatherViewModel
import com.soha.weather_app.weather.fragments.favourite.FavViewModel
import com.soha.weather_app.weather.fragments.favourite.FavouriteWeather
import com.soha.weather_app.weather.fragments.setting.MapFragment.LocationViewModel
import com.soha.weather_app.weather.fragments.setting.MapFragment.MapsFragment
import kotlinx.android.synthetic.main.fragment_home_weather.*
import kotlinx.android.synthetic.main.fragment_setting_weather.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.properties.Delegates


class SettingWeather : Fragment(R.layout.fragment_setting_weather) {

    lateinit var binding: FragmentSettingWeatherBinding
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var currentViewModel: CurrentViewModel
    lateinit var weatherViewModel: WeatherViewModel
    lateinit var favViewModel: FavViewModel

    lateinit var model: LocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ):
            View? {

        currentViewModel =  ViewModelProvider(this).get(CurrentViewModel::class.java)
        weatherViewModel =  ViewModelProvider(this).get(WeatherViewModel::class.java)
        favViewModel =  ViewModelProvider(this).get(FavViewModel::class.java)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        model = ViewModelProvider(requireActivity()).get(LocationViewModel::class.java)

        binding = FragmentSettingWeatherBinding.inflate(layoutInflater)

        model.getAddressData().observe(viewLifecycleOwner, Observer {
            binding.tvAddressLocation.text = it
        })

        model.getLatData().observe(viewLifecycleOwner, Observer {
            binding.tvLat.text = it.toString()
        })

        model.getLonData().observe(viewLifecycleOwner, Observer {
            binding.tvLon.text = it.toString()
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
               model.setTempData("metric")
            } else if (checkedId == R.id.btnKelven) {
                model.setTempData("imperial")
            }
        }

        binding.groubWind.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.btnMs) {
                model.setWindData("metric")

            } else if (checkedId == R.id.btnKmh) {
                model.setWindData("imperial")

            }
        }

        binding.groubLang.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId== R.id.btnEnglish){
                model.setLanguageData("en")
                model.localeLang("en")

            } else if(checkedId == R.id.btnArabic){
                model.setLanguageData("ar")
                model.localeLang("ar")
            }
        }

        binding.fbtnLocation.setOnClickListener {
            if (binding.tvLat.text == null || binding.tvLon.text == null) {
                Toast.makeText(context, "dddddf", Toast.LENGTH_LONG).show()
            } else {

                context?.let { it ->
                    currentViewModel.getCurrentAPIData(it,
                        model.getLatData().value.toString(),
                        model.getLonData().value.toString(),
                        model.getTempData().value.toString(),
                        model.getLanguageData().value.toString())


                }
                currentViewModel.currentLiveData.observe(viewLifecycleOwner, Observer {
                    when (it) {
                        is Resource.Success -> {

                            it.data?.let {
                                Toast.makeText(context, "${it.name}", Toast.LENGTH_LONG).show()
                                binding.tvAddressLocation.text = it.name

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

        if (context.let {
                ActivityCompat.checkSelfPermission(it!!,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED && context.let {
                ActivityCompat.checkSelfPermission(
                    it!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                model.setLatData(location.latitude)
                model.setLonData(location.longitude)
                Toast.makeText(context, "${location.latitude}", Toast.LENGTH_LONG).show()
            }

        }

    }

}