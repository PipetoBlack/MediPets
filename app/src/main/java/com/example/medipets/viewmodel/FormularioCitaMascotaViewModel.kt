package com.example.medipets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipets.R
import com.example.medipets.model.data.entities.FormularioCitaMascotaEntity
import com.example.medipets.model.data.repository.FormularioCitaMascotaRepository
import com.example.medipets.model.domain.CitaMascotaErrores
import com.example.medipets.model.domain.FormularioCitaMascotaUIState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormularioCitaMascotaViewModel(private val repository: FormularioCitaMascotaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(FormularioCitaMascotaUIState())
    val uiState: StateFlow<FormularioCitaMascotaUIState> = _uiState.asStateFlow()
    private val _eventoCanal = Channel<Evento>()
    val eventoFlujo = _eventoCanal.receiveAsFlow()
    sealed class Evento {
        data class MostrarSnackbar(val mensaje: String) : Evento()
        object NavegarAHome : Evento() // El nuevo evento de navegación
    }

    // Lógica del calendario
    fun onShowDatePicker() {
        _uiState.update { it.copy(mostrarDatePicker = true) }
    }

    fun onDismissDatePicker() {
        _uiState.update { it.copy(mostrarDatePicker = false) }
    }
    fun onDateSelected(millis: Long) {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateString = formatter.format(Date(millis))

        _uiState.update {
            it.copy(
                fecha = dateString,
                errores = it.errores.copy(fecha = null),
                mostrarDatePicker = false
            )
        }
    }

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

                _eventoCanal.send(Evento.MostrarSnackbar("¡Cita guardada con éxito!"))
                _eventoCanal.send(Evento.NavegarAHome)
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
