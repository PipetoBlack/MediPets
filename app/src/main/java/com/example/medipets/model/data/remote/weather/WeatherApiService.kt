package com.example.medipets.model.data.remote.weather

import retrofit2.http.GET

interface WeatherApiService {

    @GET("general/public/clima")
    suspend fun getWeatherList(): List<WeatherResponse>
}
