package com.example.weatherforecastapp

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var cityEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var cityTextView: TextView
    private lateinit var temperatureTextView: TextView
    private lateinit var conditionTextView: TextView
    private lateinit var humidityTextView: TextView
    private lateinit var windTextView: TextView
    private lateinit var errorTextView: TextView
    private lateinit var weatherResultContainer: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            view.setPadding(
                bars.left,
                bars.top,
                bars.right,
                bars.bottom
            )

            insets
        }

        cityEditText = findViewById(R.id.cityEditText)
        searchButton = findViewById(R.id.searchButton)
        progressBar = findViewById(R.id.progressBar)
        cityTextView = findViewById(R.id.cityTextView)
        temperatureTextView =
            findViewById(R.id.temperatureTextView)
        conditionTextView =
            findViewById(R.id.conditionTextView)
        humidityTextView =
            findViewById(R.id.humidityTextView)
        windTextView = findViewById(R.id.windTextView)
        errorTextView = findViewById(R.id.errorTextView)
        weatherResultContainer =
            findViewById(R.id.weatherResultContainer)

        searchButton.setOnClickListener {
            submitSearch()
        }

        cityEditText.setOnEditorActionListener {
                _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                submitSearch()
                true
            } else {
                false
            }
        }
    }

    private fun submitSearch() {
        val city = cityEditText.text.toString().trim()

        if (city.isEmpty()) {
            clearWeather()
            showError("Please enter a city name.")
            cityEditText.error =
                "Please enter a city name."
            return
        }

        cityEditText.error = null

        if (BuildConfig.WEATHER_API_KEY.isBlank()) {
            clearWeather()
            showError("API key is not configured.")
            return
        }

        requestWeather(city)
    }

    private fun requestWeather(city: String) {
        setLoading(true)
        clearError()

        RetrofitClient.weatherApi
            .getCurrentWeather(
                BuildConfig.WEATHER_API_KEY,
                city
            )
            .enqueue(object : Callback<WeatherResponse> {

                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    setLoading(false)

                    val weather = response.body()

                    if (response.isSuccessful &&
                        weather != null
                    ) {
                        displayWeather(weather)
                    } else {
                        clearWeather()

                        val message = when (response.code()) {
                            400 ->
                                "City not found. Check the city name."
                            401 ->
                                "API key is missing or invalid."
                            403 ->
                                "API access denied or limit reached."
                            else ->
                                "API error: ${response.code()}"
                        }

                        showError(message)
                    }
                }

                override fun onFailure(
                    call: Call<WeatherResponse>,
                    throwable: Throwable
                ) {
                    setLoading(false)
                    clearWeather()

                    showError(
                        "Network error. Check your connection."
                    )
                }
            })
    }

    private fun displayWeather(weather: WeatherResponse) {
        weatherResultContainer.visibility = View.VISIBLE

        cityTextView.text = weather.location.name

        temperatureTextView.text = String.format(
            Locale.getDefault(),
            "%.1f°C",
            weather.current.temperatureCelsius
        )

        conditionTextView.text =
            weather.current.condition.text

        humidityTextView.text =
            "Humidity\n${weather.current.humidity}%"

        windTextView.text =
            "Wind Speed\n${weather.current.windSpeedKph} km/h"

        clearError()
    }

    private fun clearWeather() {
        weatherResultContainer.visibility = View.GONE
        cityTextView.text = ""
        temperatureTextView.text = ""
        conditionTextView.text = ""
        humidityTextView.text = ""
        windTextView.text = ""
    }

    private fun showError(message: String) {
        errorTextView.text = message
        errorTextView.visibility = View.VISIBLE
    }

    private fun clearError() {
        errorTextView.text = ""
        errorTextView.visibility = View.GONE
    }

    private fun setLoading(loading: Boolean) {
        progressBar.visibility =
            if (loading) View.VISIBLE else View.GONE

        searchButton.isEnabled = !loading
    }
}