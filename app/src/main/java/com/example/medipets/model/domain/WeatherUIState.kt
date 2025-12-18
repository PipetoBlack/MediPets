package com.example.medipets.model.domain

data class WeatherUIState(
    val isLoading: Boolean = false,
    val city: String = "",
    val temperature: String = "",
    val condition: String = "",
    val errorMessage: String? = null
)