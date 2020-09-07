package com.example.klarna.home.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.klarna.R
import com.example.klarna.home.contract.HomeMvp
import com.example.klarna.home.model.WeatherData
import com.example.klarna.home.presenter.HomePresenter
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), HomeMvp.View {

    private lateinit var presenter: HomePresenter
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = FusedLocationProviderClient(this)
        presenter = HomePresenter(this)
        presenter.onCreate()
    }

    override fun requestForPermission(permissions: Array<String>, requestCode: Int) {
        when {
            hasLocationPermission() -> presenter.onRequestGranted()
            else -> requestPermissions(permissions, requestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (hasLocationPermission()) {
            presenter.onRequestGranted()
        }
    }

    @SuppressLint("MissingPermission")
    override fun setLocationManager() {
        if (hasLocationPermission()) {
            fusedLocationProviderClient.lastLocation?.addOnCompleteListener {
                presenter.onFetchLastKnownLocation(it)
            }
        }
    }

    override fun hasLocationPermission() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    override fun getGooglePlayServiceStatus() =
        GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)

    override fun showGooglePlayServiceError() {
        GoogleApiAvailability.getInstance().getErrorDialog(this, R.string.app_name, R.string.app_name)
    }

    @SuppressLint("MissingPermission")
    override fun requestLocation() {
        var callback: LocationCallback? = null
        callback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                fusedLocationProviderClient.removeLocationUpdates(callback)
                presenter.onLocationResult(locationResult)
            }
        }
        val request = LocationRequest().apply {
            interval = 500
            smallestDisplacement = 0f
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            setExpirationDuration(1000 * 30)
        }
        fusedLocationProviderClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
    }

    override fun showLoading() {
        loader.visibility = VISIBLE
    }

    override fun hideLoading() {
        loader.visibility = GONE
    }

    override fun onWeatherFetched(weatherData: WeatherData) {
        updateWeather(weatherData)
    }

    override fun onWeatherError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun updateWeather(weatherData: WeatherData) {
        tv_temp.text = "Temp: ${weatherData.currently.temperature}"
        tv_humidity.text = "Humidity: ${weatherData.currently.humidity}"
        tv_wind_speed.text = "Wind Speed: ${weatherData.currently.windSpeed.toString()}"
    }

}