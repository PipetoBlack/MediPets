package com.example.medipets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipets.model.data.entities.VeterinarioEntity
import com.example.medipets.model.data.repository.VeterinarioRepository
import com.example.medipets.model.domain.VeterinarioErrores
import com.example.medipets.model.domain.VeterinarioUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VeterinarioViewModel(
    private val repository: VeterinarioRepository
) : ViewModel() {

    // Estado del formulario
    private val _estado = MutableStateFlow(VeterinarioUIState())
    val estado: StateFlow<VeterinarioUIState> = _estado.asStateFlow()

    // Lista de veterinarios guardados
    private val _veterinarios =
        MutableStateFlow<List<VeterinarioEntity>>(emptyList())
    val veterinarios: StateFlow<List<VeterinarioEntity>> = _veterinarios.asStateFlow()

    init {
        observarVeterinarios()
    }

    // Observador de la lista
    private fun observarVeterinarios() {
        viewModelScope.launch {
            repository.obtenerVeterinarios().collectLatest { lista ->
                _veterinarios.value = lista
            }
        }
    }

    // Handlers de cambios en los campos

    fun onNombreChange(valor: String) {
        _estado.update { actual ->
            val nuevosErrores = actual.errores.copy(
                nombre = if (valor.isBlank()) "El nombre es obligatorio" else null
            )
            actual.copy(nombre = valor, errores = nuevosErrores)
        }
    }

    fun onEspecialidadChange(valor: String) {
        _estado.update { actual ->
            val nuevosErrores = actual.errores.copy(
                especialidad =
                    if (valor.isBlank()) "La especialidad es obligatoria" else null
            )
            actual.copy(especialidad = valor, errores = nuevosErrores)
        }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { actual ->
            val nuevosErrores = actual.errores.copy(
                correo = when {
                    valor.isBlank() -> "El correo es obligatorio"
                    !EMAIL_REGEX.matches(valor) -> "Formato de correo no válido"
                    else -> null
                }
            )
            actual.copy(correo = valor, errores = nuevosErrores)
        }
    }

    fun onTelefonoChange(valor: String) {
        _estado.update { actual ->
            val nuevosErrores = actual.errores.copy(
                telefono = if (valor.isBlank()) "El teléfono es obligatorio" else null
            )
            actual.copy(telefono = valor, errores = nuevosErrores)
        }
    }

    // Guardar veterinario

    fun onGuardarVeterinario() {
        val ui = _estado.value

        // Validaciones finales
        val errores = VeterinarioErrores(
            nombre = if (ui.nombre.isBlank()) "El nombre es obligatorio" else null,
            especialidad = if (ui.especialidad.isBlank()) "La especialidad es obligatoria" else null,
            correo = when {
                ui.correo.isBlank() -> "El correo es obligatorio"
                !EMAIL_REGEX.matches(ui.correo) -> "Formato de correo inválido"
                else -> null
            },
            telefono = if (ui.telefono.isBlank()) "El teléfono es obligatorio" else null
        )

        _estado.update { it.copy(errores = errores) }

        // Usamos la función que tenemos en VeterinarioErrores
        if (errores.tieneErrores()) return

        // Si no existen errores se guarda en la base de datos
        viewModelScope.launch {
            repository.guardarVeterinario(
                nombre = ui.nombre,
                especialidad = ui.especialidad,
                correo = ui.correo,
                telefono = ui.telefono
            )

            // Limpiar formulario
            _estado.value = VeterinarioUIState()
        }
    }

    //  Eliminar por ID (por si se ocupa después en la UI)

    fun eliminarVeterinarioPorId(idVeterinario: Long) {
        viewModelScope.launch {
            repository.eliminarVeterinarioPorId(idVeterinario)
        }
    }
    companion object {
        private val EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    }
}