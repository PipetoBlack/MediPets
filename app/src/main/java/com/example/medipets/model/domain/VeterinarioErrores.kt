package com.example.medipets.model.domain

data class VeterinarioErrores(
    val nombre: String? = null,
    val especialidad: String? = null,
    val correo: String? = null,
    val telefono: String? = null
) {
    fun tieneErrores(): Boolean =
        nombre != null ||
                especialidad != null ||
                correo != null ||
                telefono != null
}