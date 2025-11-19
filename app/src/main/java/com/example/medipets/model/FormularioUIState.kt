package com.example.medipets.model

data class FormularioServicioUIState(
    val nombreCliente: String = "",
    val correoCliente: String = "",
    val region: String = "",
    val errores: FormularioErrores = FormularioErrores()
)