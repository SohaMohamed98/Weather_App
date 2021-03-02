package com.soha.weather_app.weather.fragments.setting

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.soha.weather_app.R
import com.soha.weather_app.databinding.FragmentSettingWeatherBinding
import com.soha.weather_app.weather.fragments.setting.MapFragment.MapsFragment


class SettingWeather : Fragment(R.layout.fragment_setting_weather) {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var binding: FragmentSettingWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        binding = FragmentSettingWeatherBinding.inflate(layoutInflater)
        val root = binding.root
        binding.groubLocation.setOnCheckedChangeListener { group, checkedId ->

            if (checkedId == R.id.btnGps) {

                getLastKnownLocation()
                Toast.makeText(context, "gps", Toast.LENGTH_LONG).show()
            } else if (checkedId == R.id.btnLocation) {
                loadFragment(MapsFragment())


            }

        }
        return root
    }

    private fun loadFragment(fragment: Fragment) {

        val fm = fragmentManager


        val fragmentTransaction: FragmentTransaction = fm!!.beginTransaction()
        // replace the FrameLayout with new Fragment
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

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    Toast.makeText(context, "${location.latitude}", Toast.LENGTH_LONG).show()
                }

            }

    }


}