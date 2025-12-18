package com.example.medipets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipets.R
import com.example.medipets.model.data.entities.*
import com.example.medipets.model.data.repository.FormularioCitaMascotaRepository
import com.example.medipets.model.data.repository.MascotaRepository
import com.example.medipets.model.data.repository.VeterinarioRepository
import com.example.medipets.model.domain.FormularioCitaMascotaUIState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FormularioCitaMascotaViewModel(
    private val repository: FormularioCitaMascotaRepository,
    mascotaRepo: MascotaRepository,
    veterinarioRepo: VeterinarioRepository
) : ViewModel() {


    // ---------- UI STATE ----------
    private val _uiState = MutableStateFlow(FormularioCitaMascotaUIState())
    val uiState: StateFlow<FormularioCitaMascotaUIState> = _uiState.asStateFlow()

    // ---------- EVENTOS ----------
    private val _eventoCanal = Channel<Evento>(Channel.BUFFERED)
    val eventoFlujo = _eventoCanal.receiveAsFlow()

    sealed class Evento {
        data class MostrarSnackbar(val mensaje: String) : Evento()
        object NavegarAHome : Evento()
    }

    // ---------- LISTAS DESDE ROOM ----------
    val mascotas: StateFlow<List<MascotaEntity>> =
        mascotaRepo.obtenerMascotas()
            .catch { emit(emptyList()) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val veterinarios: StateFlow<List<VeterinarioEntity>> =
        veterinarioRepo.obtenerVeterinarios()
            .catch { emit(emptyList()) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    // ---------- HANDLERS ----------
    fun onMascotaSeleccionada(mascota: MascotaEntity) {
        _uiState.update {
            it.copy(
                mascotaSeleccionada = mascota,
                errores = it.errores.copy(mascota = null)
            )
        }
    }

    fun onVeterinarioSeleccionado(veterinario: VeterinarioEntity) {
        _uiState.update { it.copy(veterinarioSeleccionado = veterinario) }
    }

    fun onMotivoChange(valor: String) {
        _uiState.update {
            it.copy(
                motivo = valor,
                errores = it.errores.copy(motivo = null)
            )
        }
    }

    fun onDateSelected(millis: Long) {
        val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(Date(millis))

        _uiState.update {
            it.copy(
                fecha = fecha,
                errores = it.errores.copy(fecha = null),
                mostrarDatePicker = false
            )
        }
    }

    // ---------- GUARDAR ----------
    fun guardarCita() {
        val estado = _uiState.value

        if (estado.mascotaSeleccionada == null) {
            _uiState.update {
                it.copy(errores = it.errores.copy(mascota = R.string.error_campo_obligatorio))
            }
            return
        }

        if (estado.fecha.isBlank()) {
            _uiState.update {
                it.copy(errores = it.errores.copy(fecha = R.string.error_campo_obligatorio))
            }
            return
        }

        if (estado.motivo.isBlank()) {
            _uiState.update {
                it.copy(errores = it.errores.copy(motivo = R.string.error_campo_obligatorio))
            }
            return
        }

        viewModelScope.launch {
            val cita = FormularioCitaMascotaEntity(
                usuario_id = estado.formulario_servicio_id ?: 0,
                mascota_id = estado.mascotaSeleccionada!!.id,
                veterinario_id = estado.veterinarioSeleccionado?.id,
                fecha = estado.fecha,
                motivo = estado.motivo
            )

            repository.agendarCita(cita)

            _uiState.value = FormularioCitaMascotaUIState()

            _eventoCanal.send(Evento.MostrarSnackbar("¡Cita guardada con éxito!"))
            _eventoCanal.send(Evento.NavegarAHome)
        }



    }
    init {
        viewModelScope.launch {
            mascotas.collect {
                println("🐶 Mascotas cargadas: ${it.size}")
            }
        }
    }


    fun onShowDatePicker() {
        _uiState.update { it.copy(mostrarDatePicker = true) }
    }

    fun onDismissDatePicker() {
        _uiState.update { it.copy(mostrarDatePicker = false) }
    }
}
