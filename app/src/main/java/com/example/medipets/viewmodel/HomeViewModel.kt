package com.example.medipets.viewmodel

import androidx.lifecycle.ViewModel
import com.example.medipets.model.FormularioServicioUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {
    private val _estado = MutableStateFlow(FormularioServicioUIState())
    val estado: StateFlow<FormularioServicioUIState> = _estado

    fun onNombreChange(valor: String){
        _estado.update {
            it.copy(
                nombreCliente = valor,
                errores = it.errores.copy(nombreCliente = null)
            )
        }
    }

    fun onCorreoChange(valor: String){
        _estado.update {
            it.copy(
                correoCliente = valor,
                errores = it.errores.copy(correoCliente = null)
            )
        }
    }


}