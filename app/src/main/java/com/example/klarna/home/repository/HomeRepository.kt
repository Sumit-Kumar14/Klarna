package com.example.klarna.home.repository

import com.example.klarna.home.model.WeatherData
import retrofit2.Call

interface HomeRepository {

    fun getCurrentWeather(location: String): Call<WeatherData>

}