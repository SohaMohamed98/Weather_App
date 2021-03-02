package com.soha.weather_app.weather.fragments.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.soha.weather_app.R
import com.soha.weather_app.databinding.FragmentSettingWeatherBinding
import com.soha.weather_app.weather.fragments.MapsFragment
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_setting_weather.*


class SettingWeather : Fragment(R.layout.fragment_setting_weather) {

    lateinit var binding:FragmentSettingWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentSettingWeatherBinding.inflate(layoutInflater)
        val root = binding.root
        binding.groubLocation.setOnCheckedChangeListener { group, checkedId ->

            if(checkedId == R.id.btnGps){

                Toast.makeText(context,"gps",Toast.LENGTH_LONG).show()
            }else if(checkedId == R.id.btnLocation){
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

}