package com.example.medipets.model.domain

data class FormularioServicioErrores(
    val nombreCliente: String? = null,
    val correoCliente: String? = null,
    val region: String? = null
) {
    fun tieneErrores(): Boolean =
        nombreCliente != null || correoCliente != null || region != null
}