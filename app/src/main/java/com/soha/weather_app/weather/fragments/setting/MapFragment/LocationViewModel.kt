package com.soha.weather_app.weather.fragments.setting.MapFragment

import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import java.util.*

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    val addressLiveData = MutableLiveData<String?>()
    val latLiveData = MutableLiveData<Double>()
    val lonLiveData = MutableLiveData<Double>()
    val tempLiveData = MutableLiveData<String>()
    val windLiveData = MutableLiveData<String>()
    val languageLiveData = MutableLiveData<String>()

    fun setAddressData(address: String?) {
        addressLiveData.postValue(address)
    }

    fun getAddressData(): MutableLiveData<String?> {
        return addressLiveData
    }


    fun setLatData(lat: Double) {
        latLiveData.postValue(lat)
    }

    fun getLatData(): MutableLiveData<Double> {
        return latLiveData
    }

    fun setLonData(lon: Double) {
        lonLiveData.postValue(lon)
    }

    fun getLonData(): MutableLiveData<Double> {
        return lonLiveData
    }


    fun setTempData(temp: String) {
        tempLiveData.postValue(temp)
    }

    fun getTempData(): MutableLiveData<String> {
        return tempLiveData
    }

    fun setWindData(wind: String) {
        windLiveData.postValue(wind)
    }

    fun getWindData(): MutableLiveData<String> {
        return windLiveData
    }

    fun setLanguageData(lang: String) {
       languageLiveData.postValue(lang)
    }

    fun getLanguageData(): MutableLiveData<String> {
        return languageLiveData
    }

    fun getAddressGeocoder(lat: LatLng): String? {
        val geocoder = Geocoder(getApplication())
        val list = geocoder.getFromLocation(lat.latitude, lat.longitude, 1)
        return list[0].getAddressLine(0)

    }

    fun localeLang(language: String){
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = getApplication<Application>().resources
        val configuration = resources.getConfiguration()
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.getDisplayMetrics())
    }
}