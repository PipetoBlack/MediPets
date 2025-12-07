package com.example.medipets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipets.model.data.entities.MascotaEntity
import com.example.medipets.model.data.repository.MascotaRepository
import com.example.medipets.model.domain.MascotaErrores
import com.example.medipets.model.domain.MascotaUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MascotaViewModel(
    private val repository: MascotaRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(MascotaUIState())
    val estado: StateFlow<MascotaUIState> = _estado.asStateFlow()

    private val _mascotas = MutableStateFlow<List<MascotaEntity>>(emptyList())
    val mascotas: StateFlow<List<MascotaEntity>> = _mascotas.asStateFlow()

    init {
        observarMascotas()
    }

    private fun observarMascotas() {
        viewModelScope.launch {
            repository.obtenerMascotas().collectLatest { lista ->
                _mascotas.value = lista
            }
        }
    }

    fun onNombreChange(valor: String) {
        _estado.update { actual ->
            actual.copy(
                nombre = valor,
                errores = actual.errores.copy(
                    nombre = if (valor.isBlank()) "El nombre es obligatorio" else null
                )
            )
        }
    }

    fun onTipoChange(valor: String) {
        _estado.update { actual ->
            actual.copy(
                tipo = valor,
                errores = actual.errores.copy(
                    tipo = if (valor.isBlank()) "El tipo de animal es obligatorio" else null
                )
            )
        }
    }

    fun onRazaChange(valor: String) {
        _estado.update { actual ->
            actual.copy(
                raza = valor,
                errores = actual.errores.copy(
                    raza = if (valor.isBlank()) "La raza es obligatoria" else null
                )
            )
        }
    }

    fun onEdadChange(valor: String) {
        _estado.update { actual ->
            actual.copy(
                edad = valor,
                errores = actual.errores.copy(
                    edad = if (valor.isNotBlank() && valor.toIntOrNull() == null)
                        "Debe ser un número" else null
                )
            )
        }
    }

    fun onFotoChange(uri: String?) {
        _estado.update { it.copy(fotoUri = uri) }
    }

    fun guardarMascota() {
        val ui = _estado.value

        val errores = MascotaErrores(
            nombre = if (ui.nombre.isBlank()) "El nombre es obligatorio" else null,
            tipo   = if (ui.tipo.isBlank()) "El tipo es obligatorio" else null,
            raza   = if (ui.raza.isBlank()) "La raza es obligatoria" else null,
            edad   = if (ui.edad.isNotBlank() && ui.edad.toIntOrNull() == null)
                "Edad inválida" else null
        )

        _estado.update { it.copy(errores = errores) }

        if (errores.tieneErrores()) return

        viewModelScope.launch {
            repository.guardarMascota(
                nombre = ui.nombre,
                tipo = ui.tipo,
                raza = ui.raza,
                edad = ui.edad.toIntOrNull(),
                fotoUri = ui.fotoUri
            )
            _estado.value = MascotaUIState()
        }
    }
}