package com.example.medipets.model.domain

data class VeterinarioUIState(
    val nombre: String = "",
    val especialidad: String = "",
    val correo: String = "",
    val telefono: String = "",
    val errores: VeterinarioErrores = VeterinarioErrores()
)