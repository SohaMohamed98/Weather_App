package com.soha.weather_app.utils


import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.soha.weather_app.R
import java.text.SimpleDateFormat
import java.util.*


fun dateConverter(): String {
    var date = Calendar.getInstance().time
    var converter = SimpleDateFormat("EEE, d MMM yyyy", Locale("en"))
    var convertedDate = converter.format(date)

    return convertedDate
}

fun timeConverter(time: Long): String {
    var converter = SimpleDateFormat("hh:mm a")
    var convertedTime = converter.format(Date(time*1000))

    return convertedTime
}

fun dayConverter(time: Long) : String{
    var converter = SimpleDateFormat("EEE, d MMM yyyy hh:mm a")
    var convertedDay = converter.format(Date(time*1000))

    return convertedDay
}

fun setImage(imageview:ImageView,url:String?){
    when (url) {
        "01d" -> imageview.setImageResource(R.drawable.ic_01d)
        "01n" -> imageview.setImageResource(R.drawable.ic_01n)
        "02d" -> imageview.setImageResource(R.drawable.ic_02d)
        "02n" -> imageview.setImageResource(R.drawable.ic_02n)
        "03d" -> imageview.setImageResource(R.drawable.ic_03d)
        "03n" -> imageview.setImageResource(R.drawable.ic_03n)
        "04d" -> imageview.setImageResource(R.drawable.ic_04d)
        "04n" -> imageview.setImageResource(R.drawable.ic_04n)
        "09d" -> imageview.setImageResource(R.drawable.ic_09d)
        "09n" -> imageview.setImageResource(R.drawable.ic_09n)
        "10d" -> imageview.setImageResource(R.drawable.ic_10d)
        "10n" -> imageview.setImageResource(R.drawable.ic_10n)
        "11d" -> imageview.setImageResource(R.drawable.ic_11d)
        "11n" -> imageview.setImageResource(R.drawable.ic_11n)
        "13d" -> imageview.setImageResource(R.drawable.ic_13d)
        "13n" -> imageview.setImageResource(R.drawable.ic_13n)
        "50d" -> imageview.setImageResource(R.drawable.ic_50d)
        "50n" -> imageview.setImageResource(R.drawable.ic_50n)
    }

    fun setLayoutBackgroundImage(constraintLayout: ConstraintLayout, url: String?){
        when(url){
            "01d","02d","03d","04d","09d","10d","11d","13d","50d" -> {
                constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("ic_background_daylight","drawable", constraintLayout.context.packageName))
            }
            "01n","02n","03n","04n","09n","10n","11n","13n","50n" -> {
                constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("ic_background_night","drawable", constraintLayout.context.packageName))
            }
        }

    }


    fun setBackgroundResource(constraintLayout: ConstraintLayout, url: String){
        when(url){
            "01d" -> constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("background_sunny_weather","drawable", constraintLayout.context.packageName))
            "01n" -> constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("background_sunny_weather","drawable", constraintLayout.context.packageName))
            "02d" -> constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("background_sunny_weather","drawable", constraintLayout.context.packageName))
            "02n" -> constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("background_cloudly_weather","drawable", constraintLayout.context.packageName))
            "03d" -> constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("background_cloudly_weather","drawable", constraintLayout.context.packageName))
            "03n" -> constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("background_cloudly_weather","drawable", constraintLayout.context.packageName))
            "04d" -> constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("background_cloudly_weather","drawable", constraintLayout.context.packageName))
            "04n" -> constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("background_cloudly_weather","drawable", constraintLayout.context.packageName))
            "09d" -> constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("background_rainy_weather","drawable", constraintLayout.context.packageName))
            "09n" -> constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("background_rainy_weather","drawable", constraintLayout.context.packageName))
            "10d" -> constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("background_rainy_weather","drawable", constraintLayout.context.packageName))
            "10n" -> constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("background_rainy_weather","drawable", constraintLayout.context.packageName))
            "11d" -> constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("background_rainy_weather","drawable", constraintLayout.context.packageName))
            "11n" -> constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("background_rainy_weather","drawable", constraintLayout.context.packageName))
            "13d" -> constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("background_snowy_weather","drawable", constraintLayout.context.packageName))
            "13n" -> constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("background_snowy_weather","drawable", constraintLayout.context.packageName))
            "50d" -> constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("background_foggy_weather","drawable", constraintLayout.context.packageName))
            "50n" -> constraintLayout.setBackgroundResource(constraintLayout.resources.getIdentifier("background_foggy_weather","drawable", constraintLayout.context.packageName))
        }
    }


}
