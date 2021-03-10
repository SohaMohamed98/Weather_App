package com.soha.weather_app.weather.viewModel

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.util.*

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    val addressLiveData = MutableLiveData<String?>()
    val latLiveData = MutableLiveData<Double>()
    val lonLiveData = MutableLiveData<Double>()
    val tempLiveData = MutableLiveData<String>()
    val windLiveData = MutableLiveData<String>()
    val languageLiveData = MutableLiveData<String>()



    val celicousLiveData= MutableLiveData<String>()
    val windDegLiveData= MutableLiveData<String>()

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

    fun setTempDegree(temp: String) {
        celicousLiveData.postValue(temp)
    }

    fun getTempDegree(): MutableLiveData<String> {
        return celicousLiveData
    }

    fun setWindDegree(temp: String) {
        windDegLiveData.postValue(temp)
    }

    fun getWindDegree(): MutableLiveData<String> {
        return windDegLiveData
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




    fun localeLang(language: String){
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = getApplication<Application>().resources
        val configuration = resources.getConfiguration()
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.getDisplayMetrics())
    }
}