package com.example.medipets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipets.model.data.entities.MascotaEntity
import com.example.medipets.model.data.repository.MascotaRepository
import com.example.medipets.model.domain.MascotaErrores
import com.example.medipets.model.domain.MascotaUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MascotaViewModel(
    private val repository: MascotaRepository
) : ViewModel() {

    // ---------- UI STATE DEL FORMULARIO ----------
    private val _estado = MutableStateFlow(MascotaUIState())
    val estado: StateFlow<MascotaUIState> = _estado.asStateFlow()

    // ---------- LISTA DE MASCOTAS (ROOM + FLOW) ----------
    val mascotas: StateFlow<List<MascotaEntity>> =
        repository.obtenerMascotas()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    // ---------- HANDLERS DE FORMULARIO ----------
    fun onNombreChange(valor: String) {
        _estado.update {
            it.copy(
                nombre = valor,
                errores = it.errores.copy(
                    nombre = if (valor.isBlank()) "El nombre es obligatorio" else null
                )
            )
        }
    }

    fun onTipoChange(valor: String) {
        _estado.update {
            it.copy(
                tipo = valor,
                errores = it.errores.copy(
                    tipo = if (valor.isBlank()) "El tipo es obligatorio" else null
                )
            )
        }
    }

    fun onRazaChange(valor: String) {
        _estado.update {
            it.copy(
                raza = valor,
                errores = it.errores.copy(
                    raza = if (valor.isBlank()) "La raza es obligatoria" else null
                )
            )
        }
    }

    fun onEdadAniosChange(valor: String) {
        _estado.update {
            it.copy(
                edadAnios = valor,
                errores = it.errores.copy(
                    edadAnios = if (valor.isNotBlank() && valor.toIntOrNull() == null)
                        "Debe ser un número" else null
                )
            )
        }
    }

    fun onEdadMesesChange(valor: String) {
        _estado.update {
            it.copy(
                edadMeses = valor,
                errores = it.errores.copy(
                    edadMeses = if (valor.isNotBlank() && valor.toIntOrNull() == null)
                        "Debe ser un número" else null
                )
            )
        }
    }

    fun onFotoChange(uri: String?) {
        _estado.update { it.copy(fotoUri = uri) }
    }

    // ---------- GUARDAR ----------
    fun guardarMascota() {
        val ui = _estado.value

        val errores = MascotaErrores(
            nombre = if (ui.nombre.isBlank()) "El nombre es obligatorio" else null,
            tipo = if (ui.tipo.isBlank()) "El tipo es obligatorio" else null,
            raza = if (ui.raza.isBlank()) "La raza es obligatoria" else null,
            edadAnios = if (ui.edadAnios.isNotBlank() && ui.edadAnios.toIntOrNull() == null)
                "Edad inválida" else null,
            edadMeses = if (ui.edadMeses.isNotBlank() && ui.edadMeses.toIntOrNull() == null)
                "Edad inválida" else null
        )

        _estado.update { it.copy(errores = errores) }

        if (errores.tieneErrores()) return

        viewModelScope.launch {
            repository.guardarMascota(
                nombre = ui.nombre,
                tipo = ui.tipo,
                raza = ui.raza,
                edadAnios = ui.edadAnios.toIntOrNull(),
                edadMeses = ui.edadMeses.toIntOrNull(),
                fotoUri = ui.fotoUri
            )
            _estado.value = MascotaUIState()
        }
    }
}
