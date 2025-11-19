package com.example.medipets.model.domain

/**
 * Almacena el estado completo de la UI para la pantalla del formulario.
 * Incluye tanto los datos introducidos por el usuario como los errores de validación.
 */
data class FormularioServicioUIState(
    // Datos que el usuario introduce
    val nombreCliente: String = "",
    val correoCliente: String = "",
    val region: String = "",

    // Objeto que contiene los posibles errores de los campos de arriba
    val errores: FormularioServicioErrores = FormularioServicioErrores()
)