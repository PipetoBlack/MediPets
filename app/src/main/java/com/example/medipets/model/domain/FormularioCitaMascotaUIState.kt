package com.example.medipets.model.domain

import androidx.annotation.StringRes

// Guarda los datos que el usuario está escribiendo.
data class FormularioCitaMascotaUIState(
    val formulario_servicio_id: Long? = null, // Para saber a qué servicio pertenece
    val nombreMascota: String = "",
    val raza: String = "",
    val edad: String = "", // Usamos String para el campo de texto
    val fecha: String = "",
    val motivo: String = "",
    val errores: CitaMascotaErrores = CitaMascotaErrores()
)

// Guarda los posibles errores de validación de cada campo.
data class CitaMascotaErrores(
    @StringRes val nombreMascota: Int? = null,
    @StringRes val raza: Int? = null,
    @StringRes val edad: Int? = null,
    @StringRes val fecha: Int? = null,
    @StringRes val motivo: Int? = null
)
