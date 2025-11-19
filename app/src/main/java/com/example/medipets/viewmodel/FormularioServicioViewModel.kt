package com.example.medipets.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipets.model.domain.FormularioServicioUIState
import com.example.medipets.model.data.entities.FormularioServicioEntity
import com.example.medipets.model.data.repository.FormularioServicioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel (private val repository: FormularioServicioRepository) : ViewModel() {

    private val _estado = MutableStateFlow(FormularioServicioUIState())
    val estado: StateFlow<FormularioServicioUIState> = _estado.asStateFlow()

    fun onNombreChange(valor: String) {
        _estado.update { actual ->
            actual.copy(
                nombreCliente = valor,
                errores = actual.errores.copy(
                    nombreCliente = if (valor.isBlank()) "El nombre es obligatorio" else null
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
                        valor.isBlank() -> "El correo es obligatorio"
                        !EMAIL_REGEX.matches(valor) -> "Formato de correo no válido"
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
                    region = if (valor.isBlank()) "La región es obligatoria" else null
                )
            )
        }
    }

    fun onEnviarFormulario() {
        val ui = _estado.value

        // Validaciones básicas
        val errores = ui.errores.copy(
            nombreCliente = if (ui.nombreCliente.isBlank()) "El nombre es obligatorio" else null,
            correoCliente = if (ui.correoCliente.isBlank()) "El correo es obligatorio" else null,
            region = if (ui.region.isBlank()) "La región es obligatoria" else null
        )

        // Actualiza errores en UI
        _estado.update { it.copy(errores = errores) }

        // Si hay errores, no persistir
        if (errores.tieneErrores()) return

        // Persistir en SQLite (Room)
        viewModelScope.launch {
            val entity = FormularioServicioEntity(
                nombreCliente = ui.nombreCliente,
                correoCliente = ui.correoCliente,
                region = ui.region
            )
            repository.guardarFormulario(entity)

            // Opcional: limpiar formulario
            _estado.update { FormularioServicioUIState() }
        }
    }

    companion object {
        private val EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    }
}
