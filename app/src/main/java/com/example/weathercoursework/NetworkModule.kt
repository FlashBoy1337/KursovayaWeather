package com.example.weathercoursework

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private val cityRetrofit = Retrofit.Builder()
        .baseUrl("https://api.api-ninjas.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val weatherRetrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val cityService: CityApiService = cityRetrofit.create(CityApiService::class.java)
    val weatherService: WeatherApiService = weatherRetrofit.create(WeatherApiService::class.java)
}