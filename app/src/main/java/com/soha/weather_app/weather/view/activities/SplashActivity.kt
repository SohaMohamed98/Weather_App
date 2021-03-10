package com.soha.weather_app.weather.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.soha.weather_app.R
import com.soha.weather_app.databinding.ActivitySplashBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch {

            var animCloud = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.amin_splash_cloud)
            var animSun = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.anim_splash_sun)

            binding.imgSplashCloud.animation = animCloud
            binding.imgSplashSun.animation = animSun

            delay(3000)
            var intent = Intent(this@SplashActivity, WeatherActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}