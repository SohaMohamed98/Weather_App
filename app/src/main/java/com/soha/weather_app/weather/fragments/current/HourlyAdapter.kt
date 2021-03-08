package com.soha.weather_app.weather.fragments.current

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soha.weather_app.databinding.ForecastWeatherHourlyItemBinding
import com.soha.weather_app.utils.dayConverter
import com.soha.weather_app.weather.db.models.DailyModel.Daily
import com.soha.weather_app.utils.setImage
import com.soha.weather_app.weather.db.models.DailyModel.Hourly
import com.soha.weather_app.weather.db.models.DailyModel.WeatherResponse

class HourlyAdapter(var forecastList: List<Hourly>) :
    RecyclerView.Adapter<HourlyAdapter.ForecatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecatViewHolder {

        val view = ForecastWeatherHourlyItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)

        return ForecatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    override fun onBindViewHolder(holder: ForecatViewHolder, position: Int) {

        holder.bind(forecastList[position])

    }


    class ForecatViewHolder(var view: ForecastWeatherHourlyItemBinding) :
        RecyclerView.ViewHolder(view.root) {
        private val imageview = view.imgForecastItem


        fun bind(forecast: Hourly) {

            view.tvForecastState.text = forecast.weather.get(0).description
            view.tvForecastTemp.text = (Math.round(forecast.temp).toString())
            view.tvForecastHumidity.text = (Math.round(forecast.humidity)).toString()
            view.tvForecastPressure.text = (Math.round(forecast.pressure)).toString()
            view.tvForecastTime.text = dayConverter((forecast.dt).toLong())
            view.tvForecastFeelsTemp.text = (Math.round(forecast.feelsLike)).toString()


            val url = forecast.weather.get(0).icon
            setImage(imageview, url)


        }
    }


}
