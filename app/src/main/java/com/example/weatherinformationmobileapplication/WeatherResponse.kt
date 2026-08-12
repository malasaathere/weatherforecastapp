package com.example.weatherinformationmobileapplication

import com.google.gson.annotations.SerializedName

// Main data class representing the full JSON response from OpenWeatherMap
data class WeatherResponse(
    val name: String, // City Name
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind
)

data class Main(
    val temp: Double, // Temperature
    val humidity: Int // Humidity
)

data class Weather(
    val description: String // Weather Condition (e.g., "clear sky")
)

data class Wind(
    val speed: Double // Wind Speed
)
