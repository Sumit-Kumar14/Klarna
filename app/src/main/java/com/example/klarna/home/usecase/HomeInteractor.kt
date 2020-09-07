package com.example.klarna.home.usecase

import com.example.klarna.home.contract.HomeMvp
import com.example.klarna.home.model.WeatherData
import com.example.klarna.home.repository.HomeRepositoryImpl
import retrofit2.Callback
import retrofit2.Response

class HomeInteractor(val presenter: HomeMvp.Presenter) : HomeUseCase {

    private val repository = HomeRepositoryImpl()

    override fun getWeatherData(location: String) {
        repository.getCurrentWeather(location = location).enqueue(object : Callback<WeatherData> {
            override fun onFailure(call: retrofit2.Call<WeatherData>, t: Throwable) {
                presenter.onApiError(t.message ?: "Something went wrong")
            }

            override fun onResponse(call: retrofit2.Call<WeatherData>, response: Response<WeatherData>) {
                if(response.body() != null) {
                    presenter.onApiSuccess(response.body() as WeatherData)
                }else {
                    presenter.onApiError("Something went wrong")
                }
            }
        })
    }

}