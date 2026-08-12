package com.example.weatherinformationmobileapplication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.weatherinformationmobileapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    
    // REPLACE THIS with your own API key from openweathermap.org
    private val apiKey = "YOUR_API_KEY_HERE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSearch.setOnClickListener {
            val city = binding.etCityName.text.toString().trim()
            
            if (city.isEmpty()) {
                binding.tvError.text = getString(R.string.error_empty_city)
                binding.resultsLayout.visibility = View.GONE
            } else {
                fetchWeatherData(city)
            }
        }
    }

    private fun fetchWeatherData(city: String) {
        // Show ProgressBar and hide previous results/errors
        binding.progressBar.visibility = View.VISIBLE
        binding.tvError.text = ""
        binding.resultsLayout.visibility = View.GONE

        // Launch coroutine in lifecycleScope to call suspend function
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getCurrentWeather(city, apiKey)
                
                if (response.isSuccessful && response.body() != null) {
                    val weatherData = response.body()!!
                    displayResults(weatherData)
                } else if (response.code() == 404) {
                    binding.tvError.text = getString(R.string.error_city_not_found)
                } else {
                    binding.tvError.text = getString(R.string.error_generic)
                }
            } catch (e: IOException) {
                // Network failure
                binding.tvError.text = getString(R.string.error_no_internet)
            } catch (e: Exception) {
                // Other unexpected failures
                binding.tvError.text = getString(R.string.error_unexpected)
            } finally {
                // Always hide ProgressBar when done
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun displayResults(weather: WeatherResponse) {
        binding.resultsLayout.visibility = View.VISIBLE
        binding.tvCityName.text = weather.name
        
        // Populate TextViews using formatted strings
        binding.tvTemperature.text = getString(R.string.temp_format, weather.main.temp)
        binding.tvCondition.text = getString(R.string.condition_format, weather.weather[0].description)
        binding.tvHumidity.text = getString(R.string.humidity_format, weather.main.humidity)
        binding.tvWindSpeed.text = getString(R.string.wind_speed_format, weather.wind.speed)
    }
}
