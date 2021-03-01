package com.soha.weather_app.weather.fragments.current

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.soha.weather_app.databinding.ForecastWeatherHourlyItemBinding
import com.soha.weather_app.utils.dayConverter
import com.soha.weather_app.utils.setImage
import com.soha.weatherapp.model.Daily

class CurrentAdapter (var forecastList: List<Daily>) :
RecyclerView.Adapter<CurrentAdapter.ForecatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecatViewHolder {

        val view = ForecastWeatherHourlyItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)

        return ForecatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }



    override fun onBindViewHolder(holder: ForecatViewHolder, position: Int) {

        holder.bind(forecastList[position])

    }

   /* fun updateHourlyList(newHourlyList: List<Daily>){
        //  forecastList.clear()
        // forecastList.(newHourlyList)
        notifyDataSetChanged()
    }*/

    class ForecatViewHolder(var view: ForecastWeatherHourlyItemBinding) :
        RecyclerView.ViewHolder(view.root) {
        private val imageview = view.imgForecastItem


        fun bind(forecast: Daily) {

            view.tvForecastState.text=forecast.weather.get(0).description
            view.tvForecastFeelsTemp.text = (Math.round(forecast.temp.day)).toString()
            view.textForecastHumidity.text= (Math.round(forecast.humidity)).toString()
            view.textForecastPressure.text= (Math.round(forecast.pressure)).toString()
            view.tvForecastTime.text= dayConverter((forecast.dt).toLong())

            /* view.tvForecastHumidity.text = forecast.clouds.toString()
             view.tvForecastPressure.text = forecast.dewPoint.toString()
             view.tvForecastWind.text = (Math.round(forecast.windSpeed)).toString()
             view.tvForecastTime.text = dayConverter((forecast.dt).toLong())
             view.tvForecastTemp.text = forecast.weather.get(0).main.toString()
             view.tvForecastFeelsTemp.text = forecast.feelsLike.toString()*/

            val url = forecast.weather.get(0).icon
            setImage(imageview, url)


        }
    }


}
