package com.example.medipets.model.domain

data class PacienteUIState(
    val nombreMascota: String = "",
    val especie: String = "",
    val edad: String = "",
    val raza: String = "",
    val errores: PacienteErrores = PacienteErrores()
)