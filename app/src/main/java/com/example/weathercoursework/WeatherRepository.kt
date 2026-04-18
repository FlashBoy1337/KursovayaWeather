package com.example.weathercoursework

class WeatherRepository(
    private val cityApi: CityApiService,
    private val weatherApi: WeatherApiService
) {

    private val apiKey = BuildConfig.API_KEY

    suspend fun getWeatherData(cityName: String): Pair<CityLocation, WeatherResponse>? {
        return try {
            val cities = cityApi.getCityCoordinates(cityName, apiKey)
            if (cities.isEmpty()) return null
            val city = cities[0]

            val weather = weatherApi.getWeather(city.latitude, city.longitude)
            Pair(city, weather)
        } catch (e: Exception) {
            null
        }
    }
}