package com.example.weathercoursework

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CityApiService {
    @GET("v1/city")
    suspend fun getCityCoordinates(
        @Query("name") cityName: String,
        @Header("X-Api-Key") apiKey: String
    ): List<CityLocation>
}

interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current") current: String = "temperature_2m,wind_speed_10m,weather_code"
    ): WeatherResponse
}