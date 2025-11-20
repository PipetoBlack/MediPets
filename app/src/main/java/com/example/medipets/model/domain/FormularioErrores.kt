package com.example.medipets.model.domain

import androidx.annotation.StringRes

data class FormularioServicioErrores(
    @StringRes val nombreCliente: Int? = null,
    @StringRes val correoCliente: Int? = null,
    @StringRes val region: Int? = null
)
{
    fun tieneErrores(): Boolean =
        nombreCliente != null || correoCliente != null || region != null
}