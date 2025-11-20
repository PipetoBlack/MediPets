package com.example.medipets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipets.R
import com.example.medipets.model.data.entities.FormularioServicioEntity
import com.example.medipets.model.data.repository.FormularioServicioRepository
import com.example.medipets.model.domain.FormularioServicioUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: FormularioServicioRepository) : ViewModel() {

    private val _estado = MutableStateFlow(FormularioServicioUIState())
    val estado: StateFlow<FormularioServicioUIState> = _estado.asStateFlow()

    fun onNombreChange(valor: String) {
        _estado.update { actual ->
            actual.copy(
                nombreCliente = valor,
                errores = actual.errores.copy(
                    nombreCliente = if (valor.isBlank()) R.string.error_campo_obligatorio else null
                )
            )
        }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { actual ->
            actual.copy(
                correoCliente = valor,
                errores = actual.errores.copy(
                    correoCliente = when {
                        valor.isBlank() -> R.string.error_campo_obligatorio
                        !EMAIL_REGEX.matches(valor) -> R.string.error_formato_correo
                        else -> null
                    }
                )
            )
        }
    }

    fun onRegionChange(valor: String) {
        _estado.update { actual ->
            actual.copy(
                region = valor,
                errores = actual.errores.copy(
                    region = if (valor.isBlank()) R.string.error_region_obligatoria else null
                )
            )
        }
    }

    fun onEnviarFormulario() {
        onNombreChange(_estado.value.nombreCliente)
        onCorreoChange(_estado.value.correoCliente)
        onRegionChange(_estado.value.region)

        // Se accede al valor más reciente del estado para la comprobación.
        val estadoActual = _estado.value

        // Si la función tieneErrores() devuelve true, significa que hay un error y no continuamos.
        if (estadoActual.errores.tieneErrores()) {
            return
        }

        // Si no hay errores, procedemos a guardar en la base de datos
        viewModelScope.launch {
            val entity = FormularioServicioEntity(
                nombreCliente = estadoActual.nombreCliente,
                correoCliente = estadoActual.correoCliente,
                region = estadoActual.region
            )
            repository.guardarFormulario(entity)

            // Opcional: Limpiar el formulario para un nuevo ingreso
            _estado.update { FormularioServicioUIState() }
        }
    }

    companion object {
        // Expresión regular para validar el formato de un correo electrónico
        private val EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    }
}
