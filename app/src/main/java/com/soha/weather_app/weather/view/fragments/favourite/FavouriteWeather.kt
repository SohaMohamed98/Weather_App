package com.soha.weather_app.weather.view.fragments.favourite

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
import com.soha.weather_app.weather.db.Repository
import com.soha.weather_app.weather.db.Resource
import com.soha.weather_app.weather.db.entity.FavouriteData
import com.soha.weather_app.weather.view.fragments.current.HomeWeather
import com.soha.weather_app.weather.viewModel.LocationViewModel
import com.soha.weather_app.weather.view.fragments.setting.MapFragment.SettingWeather
import com.soha.weather_app.weather.viewModel.FavViewModel

class FavouriteWeather : Fragment(R.layout.fragment_favourite_weather), FavouriteAdapter.OnItemClickListener{

lateinit var binding: FragmentFavouriteWeatherBinding


    private var adapt: RecyclerView.Adapter<FavouriteAdapter.FavViewHolder>? = null
    private var layoutManag: RecyclerView.LayoutManager? = null
    private lateinit var favViewModel: FavViewModel
    private lateinit var locationViewModel: LocationViewModel
    lateinit var repo: Repository


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        favViewModel = ViewModelProvider(this).get(FavViewModel::class.java)
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)


        binding = FragmentFavouriteWeatherBinding.inflate(layoutInflater)
        repo = Repository()
        val root = binding.root

        context?.let {
            favViewModel.getFavAPIData(it)
        }

      /*  favViewModel.favLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { it1 -> displayDailyWeatherToRecycleView(it1)}
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    showErrorMessage(it.message)
                }
            }
        })*/

        binding.btnFav.setOnClickListener {
            loadFragment(SettingWeather())
        }
        favViewModel.favFromRoomLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { it1 -> displayDailyWeatherToRecycleView(it1)}
                }
                is Resource.Loading-> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    showErrorMessage(it.message)
                }
            }
        })
        return root
    }


    private fun initUI(data: List<FavouriteData>) {
        var dailyAdapter = FavouriteAdapter(data, this)
        binding.favRecyclerView.apply {
            layoutManag = LinearLayoutManager(context)
            layoutManager = layoutManag
            adapt = dailyAdapter
            adapter = adapt

        }
    }

    private fun displayDailyWeatherToRecycleView(data: List<FavouriteData>) {
        if (data != null) {
            initUI(data)
        }
    }



    private fun loadFragment(fragment: Fragment) {
        val fm = fragmentManager
        val fragmentTransaction: FragmentTransaction = fm!!.beginTransaction().addToBackStack(null)
        fragmentTransaction.replace(R.id.favContainer, fragment)
        fragmentTransaction.commit() // save the changes
    }

    private fun showProgressBar() {
        binding.favProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.favProgressBar.visibility = View.INVISIBLE
    }

    private fun showErrorMessage(message: String?) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show()
        System.out.println("Error is  :  ---->  " + message)
        //binding.progressBar.visibility = View.INVISIBLE
    }

    override fun onItemClick(contact: FavouriteData) {
        locationViewModel.setLatData(contact.lat)
        locationViewModel.setLonData(contact.lon)
        locationViewModel.setTempData("impirial")
        locationViewModel.setLanguageData("ar")
        loadFragment(HomeWeather())
    }

}






