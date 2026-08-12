package com.example.weatherforecastapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("current.json")
    fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") city: String
    ): Call<WeatherResponse>
}
