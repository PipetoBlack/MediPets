package com.example.medipets.model.domain

import androidx.annotation.StringRes

/**
 * Contiene los errores de validación del formulario.
 * Se usa un ID de recurso de String (@StringRes) para cada error.
 * Un valor 'null' significa que no hay error.
 */
data class FormularioServicioErrores(
    @StringRes val nombreCliente: Int? = null,
    @StringRes val correoCliente: Int? = null,
    @StringRes val region: Int? = null
)
{
    fun tieneErrores(): Boolean =
        nombreCliente != null || correoCliente != null || region != null
}