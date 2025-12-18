package com.example.medipets.model.domain

import androidx.annotation.StringRes
import com.example.medipets.model.data.entities.MascotaEntity
import com.example.medipets.model.data.entities.VeterinarioEntity

// Guarda los datos del formulario mientras el usuario los completa
data class FormularioCitaMascotaUIState(
    val formulario_servicio_id: Long? = null,
    val mascotaSeleccionada: MascotaEntity? = null,
    val veterinarioSeleccionado: VeterinarioEntity? = null,
    val fecha: String = "",
    val motivo: String = "",
    val mostrarDatePicker: Boolean = false,
    val errores: CitaMascotaErrores = CitaMascotaErrores()
)

// Guarda los posibles errores de validación
data class CitaMascotaErrores(
    val mascota: Int? = null,
    val fecha: Int? = null,
    val motivo: Int? = null
)
