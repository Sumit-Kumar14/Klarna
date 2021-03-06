package com.example.klarna.home.model

data class Currently(
    val apparentTemperature: Double,
    val cloudCover: Double,
    val dewPoint: Double,
    val humidity: Double,
    val icon: String,
    val ozone: Double,
    val precipIntensity: Int,
    val precipProbability: Int,
    val pressure: Double,
    val summary: String,
    val temperature: Double,
    val time: Int,
    val visibility: Double,
    val windBearing: Int,
    val windGust: Double,
    val windSpeed: Double
)