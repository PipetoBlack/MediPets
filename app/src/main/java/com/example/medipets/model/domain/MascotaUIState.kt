package com.example.medipets.model.domain

data class MascotaUIState(
    val nombre: String = "",
    val tipo: String = "",
    val raza: String = "",
    val edad: String = "",
    val fotoUri: String? = null,
    val errores: MascotaErrores = MascotaErrores()
)