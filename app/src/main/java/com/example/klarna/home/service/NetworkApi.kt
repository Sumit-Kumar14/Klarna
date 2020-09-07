package com.example.klarna.home.service

import com.example.klarna.home.model.WeatherData
import com.example.klarna.home.service.URLConstant.Companion.FORECAST
import com.example.klarna.home.service.URLConstant.Companion.LOCATION
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkApi {

    @GET(FORECAST)
    fun getWeatherData(@Path(LOCATION) location: String): Call<WeatherData>

}