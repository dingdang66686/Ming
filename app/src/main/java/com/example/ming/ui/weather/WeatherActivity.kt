package com.example.ming.ui.weather

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ming.databinding.ActivityWeatherBinding
import com.example.ming.databinding.ForecastItemBinding
import com.example.ming.logic.model.Weather
import com.example.ming.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding : ActivityWeatherBinding

    private val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(viewModel.locationLng.isEmpty())
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        if(viewModel.locationLat.isEmpty())
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        if(viewModel.placeName.isEmpty())
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null)
                showWeatherInfo(weather)
            else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        viewModel.refreshWeather(viewModel.locationLng,viewModel.locationLat)
    }

    private fun showWeatherInfo(weather: Weather) {
        val realtime = weather.realtime
        val daily = weather.daily
        binding.apply {
            nowLayout.apply {
                placeName.text = viewModel.placeName
                val currentTempText = "${realtime.temperature.toInt()} ℃"
                currentTemp.text = currentTempText
                currentSky.text = getSky(realtime.skycon).info
                val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
                currentAQI.text = currentPM25Text
                root.setBackgroundResource(getSky(realtime.skycon).bg)
            }
            forecastLayout.forecastContentLayout.apply {
                removeAllViews()
                val days = daily.skycon.size
                for(i in 0 until days) {
                    val skycon = daily.skycon[i]
                    val temperature = daily.temperature[i]
                    val forecastItemBinding = ForecastItemBinding.inflate(
                        layoutInflater,
                        this@apply,
                        false
                    )
                    forecastItemBinding.apply {
                        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        dateInfo.text = simpleDateFormat.format(skycon.date)
                        getSky(skycon.value).let {
                            skyIcon.setImageResource(it.icon)
                            skyInfo.text = it.info
                        }
                        temperatureInfo.text = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
                    }
                    addView(forecastItemBinding.root)
                }
            }
            lifeIndexLayout.apply {
                daily.lifeIndex.let {
                    coldRiskText.text = it.coldRisk[0].desc
                    dressingText.text = it.dressing[0].desc
                    ultravioletText.text = it.ultraviolet[0].desc
                    carWashingText.text = it.carWashing[0].desc
                }
            }
            weatherLayout.visibility = View.VISIBLE
        }
    }
}