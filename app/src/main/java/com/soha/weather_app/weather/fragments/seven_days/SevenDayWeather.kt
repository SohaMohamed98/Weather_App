package com.soha.weather_app.weather.fragments.seven_days

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soha.weather_app.R
import com.soha.weather_app.databinding.FragmentHomeWeatherBinding
import com.soha.weather_app.databinding.FragmentSevenDayWeatherBinding
import com.soha.weather_app.utils.model.Daily
import com.soha.weather_app.weather.db.Repository
import com.soha.weather_app.weather.db.Resource
import com.soha.weather_app.weather.fragments.current.CurrentAdapter
import com.soha.weather_app.weather.fragments.current.HomeViewModel

class SevenDayWeather : Fragment(R.layout.fragment_seven_day_weather) {

    lateinit var binding: FragmentSevenDayWeatherBinding


    private var adaptDaily: RecyclerView.Adapter<DailyAdapter.DailyViewHolder>? = null
    private var layoutManagDaily: RecyclerView.LayoutManager? = null
    private lateinit var homeViewModel: HomeViewModel
    lateinit var repo: Repository


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        repo = Repository()
        binding = FragmentSevenDayWeatherBinding.inflate(layoutInflater)
        val root = binding.root
        binding.recyclerViewDaily.isEnabled = false
        context?.let { homeViewModel.getWeatherAPIData(it) }

        homeViewModel.weatherFromRoomLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { it1 -> displayDailyWeatherToRecycleView(it1.daily) }
                }
                is Resource.Error -> {
                    showErrorMessage(it.message)
                }
            }
        })
        return root
    }


    private fun initUI(data: List<Daily>) {
        binding.tvCountry.text = data.get(0).weather.get(0).description
        var dailyAdapter = DailyAdapter(data)
        binding.recyclerViewDaily.apply {
            layoutManagDaily = LinearLayoutManager(context)
            layoutManager = layoutManagDaily
            adaptDaily = dailyAdapter
            adapter = adaptDaily

        }
    }

    private fun displayDailyWeatherToRecycleView(data: List<Daily>) {
        if (data != null) {
            initUI(data)
        }
    }

    private fun showErrorMessage(message: String?) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show()
        System.out.println("Error is  :  ---->  " + message)
        binding.progressBarDaily.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBarDaily.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBarDaily.visibility = View.INVISIBLE
    }


}
