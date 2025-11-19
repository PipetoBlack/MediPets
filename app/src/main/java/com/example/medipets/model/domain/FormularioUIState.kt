package com.example.medipets.model.domain

data class FormularioServicioUIState(
    val nombreCliente: String = "",
    val correoCliente: String = "",
    val region: String = "",
    val errores: FormularioErrores = FormularioErrores()
)