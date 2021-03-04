package com.soha.weather_app.weather.fragments.setting.MapFragment

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlin.math.pow

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    val addressLiveData = MutableLiveData<String?>()
    val latLiveData =MutableLiveData<Double>()
    val lonLiveData =MutableLiveData<Double>()

    fun setAddressData(address:String?){
        addressLiveData.postValue(address)
    }
    fun getAddressData(): MutableLiveData<String?> {
        return addressLiveData
    }

    fun setLatData(lat: Double){
        latLiveData.postValue(lat)
    }
    fun getLatData():MutableLiveData<Double>{
        return latLiveData
    }

    fun setLonData(lon:Double){
        lonLiveData.postValue(lon)
    }
    fun getLonData():MutableLiveData<Double>{
        return lonLiveData
    }

    fun getAddressGeocoder(lat: LatLng): String? {
        val geocoder = Geocoder(getApplication())
        val list = geocoder.getFromLocation(lat.latitude, lat.longitude,1)
        return list[0].getAddressLine(0)

    }







}