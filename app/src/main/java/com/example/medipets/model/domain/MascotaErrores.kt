package com.example.medipets.model.domain

data class MascotaErrores(
    val nombre: String? = null,
    val tipo: String? = null,
    val raza: String? = null,
    val edadAnios: String? = null,
    val edadMeses: String? = null,
    val foto: String? = null
) {
    fun tieneErrores(): Boolean =
        nombre != null || tipo != null || raza != null ||
                edadAnios != null || edadMeses != null || foto != null // <--- Incluimos foto aquí
}