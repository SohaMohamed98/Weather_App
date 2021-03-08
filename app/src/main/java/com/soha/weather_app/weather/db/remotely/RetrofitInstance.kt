package com.soha.weather_app.db.remotely

import com.soha.weather_app.utils.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object RetrofitInstance {


    //lazy: means we only initialize this here wants

    public fun getWeatherService(): WeatherAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAPI::class.java)

    }
}

/*private val retrofit by lazy {
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
}

val api by lazy {
    retrofit.create(WeatherAPI::class.java)
}
}*/
