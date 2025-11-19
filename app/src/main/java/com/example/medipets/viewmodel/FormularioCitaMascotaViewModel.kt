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

/**
 * ViewModel para la pantalla de agendar cita de mascota.
 * Gestiona el estado de la UI (los datos del formulario y los errores)
 * y se comunica con el Repository para guardar los datos.
 */
class FormularioCitaMascotaViewModel(private val repository: FormularioCitaMascotaRepository) : ViewModel() {

    // Contiene el estado actual del formulario (los textos y errores). Es privado.
    private val _uiState = MutableStateFlow(FormularioCitaMascotaUIState())

    // Versión pública y de solo lectura del estado, para que la UI lo observe.
    val uiState: StateFlow<FormularioCitaMascotaUIState> = _uiState.asStateFlow()

    // --- FUNCIONES 'onChange' ---
    // Se llaman desde la UI cada vez que el usuario escribe en un campo.
    // Actualizan el valor en el estado y limpian el error de ese campo.

    fun onNombreMascotaChange(valor: String) {
        _uiState.update { it.copy(nombreMascota = valor, errores = it.errores.copy(nombreMascota = null)) }
    }

    fun onRazaChange(valor: String) {
        _uiState.update { it.copy(raza = valor, errores = it.errores.copy(raza = null)) }
    }

    fun onEdadChange(valor: String) {
        // Permitimos solo números en el campo de edad para evitar errores.
        if (valor.all { it.isDigit() }) {
            _uiState.update { it.copy(edad = valor, errores = it.errores.copy(edad = null)) }
        }
    }

    fun onFechaChange(valor: String) {
        _uiState.update { it.copy(fecha = valor, errores = it.errores.copy(fecha = null)) }
    }

    fun onMotivoChange(valor: String) {
        _uiState.update { it.copy(motivo = valor, errores = it.errores.copy(motivo = null)) }
    }

    // Se llama cuando el usuario presiona el botón de guardar.
    fun guardarCita() {
        // Primero, valida los campos. Si todo está bien...
        if (validarCampos()) {
            // Inicia una corutina para no bloquear la pantalla.
            viewModelScope.launch {
                val estadoActual = _uiState.value
                // Crea el objeto `Entity` que se guardará en la base de datos.
                val nuevaCita = FormularioCitaMascotaEntity(
                    // Aquí asumimos que el ID del servicio se pasa al crear el formulario.
                    // Si no, podríamos necesitar una lógica diferente. Usamos 0 como valor por defecto.
                    // ¡IMPORTANTE! Este ID debe venir de la selección de un servicio previo.
                    formulario_servicio_id = estadoActual.formulario_servicio_id ?: 0,
                    nombreMascota = estadoActual.nombreMascota,
                    raza = estadoActual.raza,
                    edad = estadoActual.edad.toIntOrNull() ?: 0, // Convierte la edad a número.
                    fecha = estadoActual.fecha,
                    motivo = estadoActual.motivo
                )

                // Llama al repositorio para que inserte la cita en la base de datos.
                repository.insert(nuevaCita)

                // (Opcional) Limpia el formulario después de guardar exitosamente.
                limpiarFormulario()

                // (Opcional) Aquí podrías emitir un evento para navegar a otra pantalla.
            }
        }
    }

    // Función privada para validar que los campos no estén vacíos.
    private fun validarCampos(): Boolean {
        val estado = _uiState.value
        // Crea un objeto de errores, asignando un ID de error si el campo está en blanco.
        val errores = CitaMascotaErrores(
            nombreMascota = if (estado.nombreMascota.isBlank()) R.string.error_campo_obligatorio else null,
            raza = if (estado.raza.isBlank()) R.string.error_campo_obligatorio else null,
            edad = if (estado.edad.isBlank()) R.string.error_campo_obligatorio else null,
            fecha = if (estado.fecha.isBlank()) R.string.error_campo_obligatorio else null,
            motivo = if (estado.motivo.isBlank()) R.string.error_campo_obligatorio else null
        )
        // Actualiza el estado de la UI con los nuevos errores encontrados.
        _uiState.update { it.copy(errores = errores) }

        // Devuelve `true` solo si todos los campos de error son `null`.
        return errores.nombreMascota == null && errores.raza == null && errores.edad == null && errores.fecha == null && errores.motivo == null
    }

    // Restablece todos los campos del formulario a su estado inicial.
    private fun limpiarFormulario() {
        _uiState.value = FormularioCitaMascotaUIState()
    }
}

