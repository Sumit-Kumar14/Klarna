package com.example.klarna.home.repository

import com.example.klarna.home.model.WeatherData
import com.example.klarna.home.service.Network
import retrofit2.Call

class HomeRepositoryImpl : HomeRepository {

    override fun getCurrentWeather(location: String): Call<WeatherData> {
        return Network.getClient().getWeatherData(location = location)
    }

}