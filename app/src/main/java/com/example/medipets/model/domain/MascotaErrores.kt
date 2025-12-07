package com.example.medipets.model.domain

data class MascotaErrores(
    val nombre: String? = null,
    val tipo: String? = null,
    val raza: String? = null,
    val edad: String? = null
) {
    fun tieneErrores(): Boolean =
        nombre != null || tipo != null || raza != null || edad != null
}