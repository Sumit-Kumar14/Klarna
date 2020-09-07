package com.example.klarna.home.presenter

import android.Manifest
import android.location.Location
import com.example.klarna.home.contract.HomeMvp
import com.example.klarna.home.model.WeatherData
import com.example.klarna.home.usecase.HomeInteractor
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.location.LocationResult
import com.google.android.gms.tasks.Task

private const val LOCATION_PERMISSION_REQ_CODE = 1001

class HomePresenter(private val view: HomeMvp.View) : HomeMvp.Presenter {

    private val homeUseCase = HomeInteractor(this)

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate() {
        view.requestForPermission(permissions, LOCATION_PERMISSION_REQ_CODE)
    }

    override fun onRequestGranted() {
        when (view.getGooglePlayServiceStatus()) {
            ConnectionResult.SERVICE_MISSING,
            ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED,
            ConnectionResult.SERVICE_DISABLED -> view.showGooglePlayServiceError()
            else -> view.run {
                showLoading()
                setLocationManager()
            }
        }
    }

    override fun onLocationChanged(location: Location?) {
        location?.let {
            val locationPath = "${it.latitude}, ${it.latitude}"
            homeUseCase.getWeatherData(location = locationPath)
        }
    }

    override fun onFetchLastKnownLocation(task: Task<Location>) {
        if (task.isSuccessful) {
            val location = task.result
            if (location != null) {
                onLocationChanged(location)
            } else {
                view.requestLocation()
            }
        } else {
            view.requestLocation()
        }
    }

    override fun onLocationResult(locationResult: LocationResult?) {
        if (null != locationResult && locationResult.locations.isNotEmpty()) {
            onLocationChanged(locationResult.locations[0])
        }
    }

    override fun onApiSuccess(weatherData: WeatherData) {
        view.hideLoading()
        view.onWeatherFetched(weatherData)
    }

    override fun onApiError(msg: String) {
        view.hideLoading()
        view.onWeatherError(msg)
    }
}