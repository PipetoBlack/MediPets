package com.example.medipets.model.data.repository

import com.example.medipets.model.data.remote.weather.WeatherApiService
import com.example.medipets.model.data.remote.weather.WeatherResponse

class WeatherRepository(
    private val api: WeatherApiService
) {

    suspend fun getWeatherForCity(city: String): Result<WeatherResponse> {
        if (city.isBlank()) {
            return Result.failure(IllegalArgumentException("La ciudad no puede estar vacía"))
        }

        return try {
            val list = api.getWeatherList()

            val match = list.firstOrNull {
                it.Estacion.contains(city, ignoreCase = true)
            } ?: return Result.failure(Exception("Ciudad no encontrada"))

            Result.success(match)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
