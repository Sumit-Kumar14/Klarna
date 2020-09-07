package com.example.klarna.home.model

data class WeatherData(
    val currently: Currently,
    val latitude: Double,
    val longitude: Double,
    val timezone: String
)