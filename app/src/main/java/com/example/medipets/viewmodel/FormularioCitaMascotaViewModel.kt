package com.example.medipets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipets.R
import com.example.medipets.model.data.entities.FormularioCitaMascotaEntity
import com.example.medipets.model.data.repository.FormularioCitaMascotaRepository
import com.example.medipets.model.domain.CitaMascotaErrores
import com.example.medipets.model.domain.FormularioCitaMascotaUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormularioCitaMascotaViewModel(private val repository: FormularioCitaMascotaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(FormularioCitaMascotaUIState())
    val uiState: StateFlow<FormularioCitaMascotaUIState> = _uiState.asStateFlow()

    // --- Lógica para el calendario ---

    // ✅ 1. Función para decirle a la UI que MUESTRE el calendario
    fun onShowDatePicker() {
        _uiState.update { it.copy(mostrarDatePicker = true) }
    }

    // ✅ 2. Función para decirle a la UI que OCULTE el calendario
    fun onDismissDatePicker() {
        _uiState.update { it.copy(mostrarDatePicker = false) }
    }

    // ✅ 3. Función para recibir la fecha seleccionada del calendario
    // El 'Long' es el timestamp en milisegundos que nos da el DatePicker
    fun onDateSelected(millis: Long) {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateString = formatter.format(Date(millis))

        // Actualizamos el estado con la nueva fecha y limpiamos el error
        _uiState.update {
            it.copy(
                fecha = dateString,
                errores = it.errores.copy(fecha = null),
                mostrarDatePicker = false // Ocultamos el diálogo después de seleccionar
            )
        }
    }


    // --- Funciones 'onChange' para los otros campos (sin cambios) ---

    fun onNombreMascotaChange(valor: String) {
        _uiState.update { it.copy(nombreMascota = valor, errores = it.errores.copy(nombreMascota = null)) }
    }

    fun onRazaChange(valor: String) {
        _uiState.update { it.copy(raza = valor, errores = it.errores.copy(raza = null)) }
    }

    fun onEdadChange(valor: String) {
        if (valor.all { it.isDigit() }) {
            _uiState.update { it.copy(edad = valor, errores = it.errores.copy(edad = null)) }
        }
    }

    fun onMotivoChange(valor: String) {
        _uiState.update { it.copy(motivo = valor, errores = it.errores.copy(motivo = null)) }
    }

    // --- Lógica de guardado (sin cambios) ---

    fun guardarCita() {
        if (validarCampos()) {
            viewModelScope.launch {
                val estadoActual = _uiState.value
                val nuevaCita = FormularioCitaMascotaEntity(
                    formulario_servicio_id = estadoActual.formulario_servicio_id ?: 0,
                    nombreMascota = estadoActual.nombreMascota,
                    raza = estadoActual.raza,
                    edad = estadoActual.edad.toIntOrNull() ?: 0,
                    fecha = estadoActual.fecha,
                    motivo = estadoActual.motivo
                )
                repository.insert(nuevaCita)
                limpiarFormulario()

            }
        }
    }


    private fun validarCampos(): Boolean {
        val estado = _uiState.value
        val errores = CitaMascotaErrores(
            nombreMascota = if (estado.nombreMascota.isBlank()) R.string.error_campo_obligatorio else null,
            raza = if (estado.raza.isBlank()) R.string.error_campo_obligatorio else null,
            edad = if (estado.edad.isBlank()) R.string.error_campo_obligatorio else null,
            fecha = if (estado.fecha.isBlank()) R.string.error_campo_obligatorio else null,
            motivo = if (estado.motivo.isBlank()) R.string.error_campo_obligatorio else null
        )
        _uiState.update { it.copy(errores = errores) }
        return errores.nombreMascota == null && errores.raza == null && errores.edad == null && errores.fecha == null && errores.motivo == null
    }

    private fun limpiarFormulario() {
        // Al limpiar, también reseteamos el estado del DatePicker
        _uiState.value = FormularioCitaMascotaUIState()
    }
}
