package com.example.weatherinformationmobileapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singleton object to provide a single instance of Retrofit throughout the app
object RetrofitClient {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}
