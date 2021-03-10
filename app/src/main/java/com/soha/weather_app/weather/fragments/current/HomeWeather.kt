package com.soha.weather_app.weather.fragments.current


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.soha.weather_app.R
import com.soha.weather_app.databinding.FragmentHomeWeatherBinding
import com.soha.weather_app.utils.dayConverter
import com.soha.weather_app.utils.setImage
import com.soha.weather_app.utils.timeConverter
import com.soha.weather_app.weather.db.Resource
import com.soha.weather_app.weather.db.Repository
import com.soha.weather_app.weather.db.model.Hourly
import com.soha.weather_app.weather.db.model.entity.WeatherResponse
import com.soha.weather_app.weather.fragments.setting.MapFragment.LocationViewModel


class HomeWeather : Fragment(R.layout.fragment_home_weather) {
    lateinit var binding: FragmentHomeWeatherBinding


    private var adapt: RecyclerView.Adapter<HourlyAdapter.ForecatViewHolder>? = null
    private var layoutManag: RecyclerView.LayoutManager? = null
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var locationViewModel:LocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)

        val temp = locationViewModel.getTempData().value.toString()
        val wind=locationViewModel.getWindData().value.toString()
        if(temp == "metric"){
            binding.textCelcius.text= "°C"

        }else if(temp=="imperial"){
            binding.textCelcius.text = "°F"
        }


        binding = FragmentHomeWeatherBinding.inflate(layoutInflater)
        val root = binding.root
        binding.recyclerViewCurrent.isEnabled = false
        context?.let {
            weatherViewModel.getWeatherAPIData(it)
           // currentViewModel.getCurrentAPIData(it,"32.154872","31.25415")
        }

        weatherViewModel.weatherLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let {
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    showErrorMessage(it.message)
                }
            }
        })




        weatherViewModel.weatherFromRoomLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    it.data?.let {
                        displayDailyWeatherToRecycleView(it.hourly)
                        displayCurrentWeatherToCard(it)
                    }
                    // it.data?.let { it1 -> initUI(it1) }
                }
                is Resource.Error -> {
                    showErrorMessage(it.message)
                }
            }
        })



        return root
    }

    private fun displayCurrentWeatherToCard(it1: WeatherResponse) {
        binding.tvAddress.text = it1.timezone
        binding.tvTemp.text = Math.round(it1.daily.get(0).temp.day).toString()
        binding.tvTempMax.text = Math.round(it1.daily.get(0).temp.max).toString()
        binding.tvTempMin.text = Math.round(it1.daily.get(0).temp.min).toString()
        binding.tvHumidity.text = Math.round(it1.current.humidity).toString()
        binding.tvUpdatedAt.text= dayConverter(it1.current.dt.toLong())
        binding.tvStatus.text =it1.current.weather.get(0).description
        binding.tvWind.text = Math.round(it1.current.windSpeed).toString()
        binding.tvPressure.text = Math.round(it1.current.pressure).toString()
        binding.tvSunrise.text= timeConverter(it1.current.sunrise.toLong())
        binding.tvSunset.text= timeConverter(it1.current.sunset.toLong())
        binding.tvVis.text = it1.current.visibility.toString()//mm
        val url = it1.current.weather.get(0).icon

        setImage(binding.imgCurrentItem,  url)
    }



    @SuppressLint("WrongConstant")
    private fun initUI(data: List<Hourly>) {
        var dailyAdapter = HourlyAdapter(data)
        binding.recyclerViewCurrent.apply {
            layoutManag = LinearLayoutManager(context,OrientationHelper.HORIZONTAL,false)
            layoutManager = layoutManag
            adapt = dailyAdapter
            adapter = adapt

        }
    }

    fun displayDailyWeatherToRecycleView(data: List<Hourly>) {
        if (data != null) {
            initUI(data)
        }
    }

    private fun showErrorMessage(message: String?) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show()
        System.out.println("Error is  :  ---->  " + message)
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }


}

