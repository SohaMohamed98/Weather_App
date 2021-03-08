package com.soha.weather_app.weather.fragments.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soha.weather_app.R
import com.soha.weather_app.databinding.FragmentFavouriteWeatherBinding
import com.soha.weather_app.databinding.FragmentSevenDayWeatherBinding
import com.soha.weather_app.weather.db.Repository
import com.soha.weather_app.weather.db.Resource
import com.soha.weather_app.weather.db.models.DailyModel.Daily
import com.soha.weather_app.weather.db.models.DailyModel.WeatherResponse
import com.soha.weather_app.weather.db.models.currentModel.CurrentResponse
import com.soha.weather_app.weather.db.models.currentModel.WeatherX
import com.soha.weather_app.weather.fragments.current.CurrentViewModel
import com.soha.weather_app.weather.fragments.current.HomeWeather
import com.soha.weather_app.weather.fragments.current.WeatherViewModel
import com.soha.weather_app.weather.fragments.seven_days.DailyAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteWeather : Fragment(R.layout.fragment_favourite_weather) ,FavouriteAdapter.OnFavouriteItemClickListner{
    //lateinit var databinding:FragmentFavouriteWeatherBinding

//        databinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite_weather, container, false)
//        var root = databinding.root
//
//        var cityDailyResponse = arguments?.getParcelable<Daily>("cityWeatherDetail")
//        databinding.detail= cityDailyResponse
//        return root
lateinit var binding: FragmentFavouriteWeatherBinding


    private var adaptDaily: RecyclerView.Adapter<FavouriteAdapter.FavViewHolder>? = null
    private var layoutManagDaily: RecyclerView.LayoutManager? = null
    private lateinit var weatherViewModel: FavViewModel
    lateinit var repo: Repository


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        weatherViewModel = ViewModelProvider(this).get(FavViewModel::class.java)
        repo = Repository()
        binding = FragmentFavouriteWeatherBinding.inflate(layoutInflater)
        val root = binding.root
        binding.favRecyclerView.isEnabled = false
        context?.let { weatherViewModel.readAllLiveData }

        weatherViewModel.readAllLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { it1 -> displayDailyWeatherToRecycleView(it1) }
                }
                is Resource.Error -> {
                    showErrorMessage(it.message)
                }
            }
        })
        return root
    }


    private fun initUI(data: List<CurrentResponse>) {

        var dailyAdapter = FavouriteAdapter(data,this)
        binding.favRecyclerView.apply {
            layoutManagDaily = LinearLayoutManager(context)
            layoutManager = layoutManagDaily
            adaptDaily = dailyAdapter
            adapter = adaptDaily

        }
    }

    private fun displayDailyWeatherToRecycleView(data: List<CurrentResponse>) {
        if (data != null) {
            initUI(data)
        }
    }

    private fun showErrorMessage(message: String?) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show()
        System.out.println("Error is  :  ---->  " + message)

    }

    override fun onItemClick(item: CurrentResponse, position: Int) {
        Toast.makeText(context,"frrrr",Toast.LENGTH_LONG).show()
        loadFragment(HomeWeather())
    }


    private fun loadFragment(fragment: Fragment) {
        val fm = fragmentManager
        val fragmentTransaction: FragmentTransaction = fm!!.beginTransaction().addToBackStack(null)
        fragmentTransaction.replace(R.id.mapContainer, fragment)
        fragmentTransaction.commit() // save the changes
    }


}

