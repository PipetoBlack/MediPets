package com.example.medipets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipets.model.data.repository.UsuarioRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class RegistroViewModel(private val repo: UsuarioRepository) : ViewModel() {

    var registroExitoso by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf<String?>(null)
        private set

    fun registrar(nombre: String, email: String, password: String) {
        viewModelScope.launch {

            if (repo.existeEmail(email)) {
                mensajeError = "El correo ya está registrado."
                registroExitoso = false
                return@launch
            }

            repo.registrarUsuario(nombre, email, password)
            mensajeError = null
            registroExitoso = true
        }
    }
}
