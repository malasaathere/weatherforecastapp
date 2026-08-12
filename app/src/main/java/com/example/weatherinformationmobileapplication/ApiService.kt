package com.example.weatherinformationmobileapplication

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit interface to define the API endpoints
interface ApiService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>
}
