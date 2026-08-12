package com.example.weatherforecastapp

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val location: Location,
    val current: CurrentWeather
)

data class Location(
    val name: String
)

data class CurrentWeather(
    @SerializedName("temp_c") val temperatureCelsius: Double,
    val condition: WeatherCondition,
    val humidity: Int,
    @SerializedName("wind_kph") val windSpeedKph: Double
)

data class WeatherCondition(
    val text: String
)
