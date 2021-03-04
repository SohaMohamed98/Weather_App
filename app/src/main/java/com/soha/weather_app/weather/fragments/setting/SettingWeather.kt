package com.soha.weather_app.weather.fragments.setting

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
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
import com.soha.weather_app.weather.fragments.setting.MapFragment.LocationViewModel
import com.soha.weather_app.weather.fragments.setting.MapFragment.MapsFragment
import kotlinx.android.synthetic.main.fragment_setting_weather.*
import kotlin.properties.Delegates


class SettingWeather : Fragment(R.layout.fragment_setting_weather) {

    lateinit var binding: FragmentSettingWeatherBinding
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var lat by Delegates.notNull<Double>()
    var lon by Delegates.notNull<Double>()
    lateinit var model: LocationViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ):
            View? {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        model = ViewModelProvider(requireActivity()).get(LocationViewModel::class.java)
        model.getAddressData().observe(viewLifecycleOwner, Observer {
            binding.tvAddressLocation.text = it
        })

        model.getLatData().observe(viewLifecycleOwner, Observer {
            lat = it
            binding.tvLat.text = it.toString()
        })

        model.getLonData().observe(viewLifecycleOwner, Observer {
            lon = it
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