package com.example.medipets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipets.model.domain.PacienteErrores
import com.example.medipets.model.domain.PacienteUIState
import com.example.medipets.model.data.entities.PacienteEntity
import com.example.medipets.model.data.repository.PacienteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PacienteViewModel(
    private val repository: PacienteRepository
) : ViewModel() {

    // Estado del formulario de paciente
    private val _estado = MutableStateFlow(PacienteUIState())
    val estado: StateFlow<PacienteUIState> = _estado.asStateFlow()

    // Lista de pacientes guardados en SQLite
    private val _pacientes = MutableStateFlow<List<PacienteEntity>>(emptyList())
    val pacientes: StateFlow<List<PacienteEntity>> = _pacientes.asStateFlow()

    init {
        observarPacientes()
    }

    //   Observar la lista de pacientes
    private fun observarPacientes() {
        viewModelScope.launch {
            repository.obtenerPacientes().collectLatest { lista ->
                _pacientes.value = lista
            }
        }
    }

    //   Handlers de cambios

    fun onNombreMascotaChange(valor: String) {
        _estado.update { actual ->
            actual.copy(
                nombreMascota = valor,
                errores = actual.errores.copy(
                    nombreMascota = if (valor.isBlank()) "El nombre es obligatorio" else null
                )
            )
        }
    }

    fun onEspecieChange(valor: String) {
        _estado.update { actual ->
            actual.copy(
                especie = valor,
                errores = actual.errores.copy(
                    especie = if (valor.isBlank()) "La especie es obligatoria" else null
                )
            )
        }
    }

    fun onEdadChange(valor: String) {
        _estado.update { actual ->
            val errorEdad =
                if (valor.isBlank()) "La edad es obligatoria"
                else if (valor.toIntOrNull() == null) "La edad debe ser un número"
                else null

            actual.copy(
                edad = valor,
                errores = actual.errores.copy(edad = errorEdad)
            )
        }
    }

    fun onRazaChange(valor: String) {
        _estado.update { actual ->
            actual.copy(raza = valor)  // Raza es opcional, por eso no tiene error
        }
    }

    // Guardar paciente

    fun onGuardarPaciente() {
        val ui = _estado.value

        // Validaciones finales
        val errores = ui.errores.copy(
            nombreMascota =
                if (ui.nombreMascota.isBlank()) "El nombre es obligatorio" else null,
            especie =
                if (ui.especie.isBlank()) "La especie es obligatoria" else null,
            edad =
                if (ui.edad.isBlank()) "La edad es obligatoria"
                else if (ui.edad.toIntOrNull() == null) "La edad debe ser un número"
                else null
        )

        // Actualizar errores en UI
        _estado.update { it.copy(errores = errores) }

        // Si hay errores, no persisten
        if (errores.tieneErrores()) return

        // Persistir en SQLite (Room)
        val edadInt = ui.edad.toInt()  // Seguro porque ya validamos

        viewModelScope.launch {
            repository.guardarPaciente(
                nombre = ui.nombreMascota,
                especie = ui.especie,
                raza = ui.raza.ifBlank { null },
                edad = edadInt
            )
            _estado.update { PacienteUIState() }
        }
    }

    // Eliminar paciente por el ID

    fun eliminarPacientePorId(idPaciente: Long) {
        viewModelScope.launch {
            repository.eliminarPacientePorId(idPaciente)
        }
    }
}