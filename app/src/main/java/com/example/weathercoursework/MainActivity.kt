package com.example.weathercoursework

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var etCityName: TextInputEditText
    private lateinit var btnSearch: Button
    private lateinit var tvCityName: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var tvWindSpeed: TextView
    private lateinit var tvWeatherStatus: TextView
    private lateinit var progressBar: ProgressBar

    private val repository = WeatherRepository(
        NetworkModule.cityService,
        NetworkModule.weatherService
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        initViews()

        btnSearch.setOnClickListener {
            val cityName = etCityName.text.toString().trim()
            if (cityName.isNotEmpty()) {
                fetchWeatherData(cityName)
            } else {
                Toast.makeText(this, getString(R.string.error_empty_input), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initViews() {
        etCityName = findViewById(R.id.etCityName)
        btnSearch = findViewById(R.id.btnSearch)
        tvCityName = findViewById(R.id.tvCityName)
        tvTemperature = findViewById(R.id.tvTemperature)
        tvWindSpeed = findViewById(R.id.tvWindSpeed)
        tvWeatherStatus = findViewById(R.id.tvWeatherStatus)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun fetchWeatherData(cityName: String) {
        hideKeyboard()
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val result = repository.getWeatherData(cityName)
                progressBar.visibility = View.GONE

                if (result != null) {
                    val (city, weather) = result
                    tvCityName.text = city.name
                    tvTemperature.text = getString(R.string.temp_format, weather.current.temperature_2m)
                    tvWindSpeed.text = getString(R.string.wind_speed_format, weather.current.wind_speed_10m)
                    tvWeatherStatus.text = parseWeatherCode(weather.current.weather_code)
                } else {
                    Toast.makeText(this@MainActivity, getString(R.string.error_not_found), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@MainActivity, getString(R.string.error_network), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun parseWeatherCode(code: Int): String {
        return when (code) {
            0 -> getString(R.string.status_clear)
            1, 2, 3 -> getString(R.string.status_cloudy)
            45, 48 -> getString(R.string.status_fog)
            51, 53, 55 -> getString(R.string.status_drizzle)
            61, 63, 65 -> getString(R.string.status_rain)
            71, 73, 75 -> getString(R.string.status_snow)
            95, 96, 99 -> getString(R.string.status_storm)
            else -> getString(R.string.status_unknown)
        }
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}