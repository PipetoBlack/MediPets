package com.example.medipets.model.domain

data class PacienteErrores(
    val nombreMascota: String? = null,
    val especie: String? = null,
    val edad: String? = null,
) {
    fun tieneErrores(): Boolean =
        nombreMascota != null ||
                especie != null ||
                edad != null
}