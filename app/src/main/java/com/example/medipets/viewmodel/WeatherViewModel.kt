package com.example.medipets.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipets.model.data.repository.WeatherRepository
import com.example.medipets.model.domain.WeatherUIState
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    var uiState by mutableStateOf(WeatherUIState())
        private set

    fun loadWeather(city: String) {
        if (city.isBlank()) {
            uiState = uiState.copy(errorMessage = "Debes especificar una ciudad")
            return
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val result = repository.getWeatherForCity(city)

            result.onSuccess { response ->
                uiState = uiState.copy(
                    isLoading = false,
                    city = response.Estacion,
                    temperature = "${response.Temp} °C",
                    condition = response.Estado,
                    errorMessage = null
                )
            }.onFailure { error ->
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "Error desconocido"
                )
            }
        }
    }
}
