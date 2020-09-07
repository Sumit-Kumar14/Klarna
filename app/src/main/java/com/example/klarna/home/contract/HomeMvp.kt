package com.example.klarna.home.contract

import android.location.Location
import com.example.klarna.home.model.WeatherData
import com.google.android.gms.location.LocationResult
import com.google.android.gms.tasks.Task

interface HomeMvp {

    interface View {
        fun requestForPermission(permissions: Array<String>, requestCode: Int)
        fun setLocationManager()
        fun hasLocationPermission(): Boolean
        fun getGooglePlayServiceStatus(): Int
        fun showGooglePlayServiceError()
        fun requestLocation()
        fun showLoading()
        fun hideLoading()
        fun onWeatherFetched(weatherData: WeatherData)
        fun onWeatherError(msg: String)
    }

    interface Presenter {
        fun onCreate()
        fun onRequestGranted()
        fun onLocationChanged(location: Location?)
        fun onFetchLastKnownLocation(task: Task<Location>)
        fun onLocationResult(locationResult: LocationResult?)
        fun onApiSuccess(weatherData: WeatherData)
        fun onApiError(msg: String)
    }

}