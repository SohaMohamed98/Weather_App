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
import com.soha.weather_app.weather.db.models.DailyModel.Hourly
import com.soha.weather_app.weather.db.models.currentModel.CurrentResponse
import com.soha.weather_app.weather.fragments.setting.MapFragment.LocationViewModel


class HomeWeather : Fragment(R.layout.fragment_home_weather) {
    lateinit var binding: FragmentHomeWeatherBinding


    private var adapt: RecyclerView.Adapter<HourlyAdapter.ForecatViewHolder>? = null
    private var layoutManag: RecyclerView.LayoutManager? = null
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var currentViewModel: CurrentViewModel
    private lateinit var locationViewModel:LocationViewModel
    lateinit var repo: Repository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        currentViewModel = ViewModelProvider(this).get(CurrentViewModel::class.java)
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
            currentViewModel.getCurrentAPIData(it)
           // currentViewModel.getCurrentAPIData(it,"32.154872","31.25415")
        }

        weatherViewModel.weatherLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let {
                        // textView.text = it.current.weather.get(0).description
                        // System.out.println("" + it.current.humidity)
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

        currentViewModel.currentLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let {
                        // textView.text = it.current.weather.get(0).description
                        // System.out.println("" + it.current.humidity)
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
                    it.data?.let { it1 -> displayDailyWeatherToRecycleView(it1.hourly) }
                    // it.data?.let { it1 -> initUI(it1) }
                }
                is Resource.Error -> {
                    showErrorMessage(it.message)
                }
            }
        })

        currentViewModel.currentLiveDataFromRoom.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { it1 -> displayCurrentWeatherToCard(it1) }
                    // it.data?.let { it1 -> initUI(it1) }
                }
                is Resource.Error -> {
                    showErrorMessage(it.message)
                }
            }
        })

        return root
    }

    private fun displayCurrentWeatherToCard(it1: CurrentResponse) {
        binding.tvAddress.text = it1.name
        binding.tvTemp.text = Math.round(it1.main.temp).toString()
        binding.tvTempMax.text = Math.round(it1.main.tempMax).toString()
        binding.tvTempMin.text = Math.round(it1.main.tempMin).toString()
        binding.tvHumidity.text = Math.round(it1.main.humidity).toString()
        binding.tvUpdatedAt.text= dayConverter(it1.dt.toLong())
        binding.tvStatus.text =it1.weather.get(0).description
        binding.tvWind.text = Math.round(it1.wind.speed).toString()
        binding.tvPressure.text = Math.round(it1.main.pressure).toString()
        binding.tvSunrise.text= timeConverter(it1.sys.sunrise.toLong())
        binding.tvSunset.text= timeConverter(it1.sys.sunset.toLong())
        binding.tvVis.text = it1.visibility.toString()//mm
        val url = it1.weather.get(0).icon

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

