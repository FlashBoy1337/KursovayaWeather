package com.example.weathercoursework


data class CityLocation(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String
)


data class WeatherResponse(
    val current: CurrentWeather
)

data class CurrentWeather(
    val temperature_2m: Double,
    val wind_speed_10m: Double,
    val weather_code: Int
)