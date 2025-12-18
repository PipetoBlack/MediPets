package com.example.medipets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medipets.model.data.remote.weather.WeatherApiClient
import com.example.medipets.model.data.repository.WeatherRepository

class WeatherViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            val api = WeatherApiClient.api
            val repository = WeatherRepository(api)

            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}