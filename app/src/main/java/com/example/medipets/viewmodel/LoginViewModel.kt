package com.example.medipets.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipets.model.data.config.AppDatabase
import com.example.medipets.model.domain.LoginUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    context: Context
) : ViewModel() {

    private val dao = AppDatabase.getDatabase(context).usuarioDao()

    private val _estado = MutableStateFlow(LoginUIState())
    val estado: StateFlow<LoginUIState> = _estado.asStateFlow()

    fun onEmailChange(v: String) =
        _estado.update { it.copy(email = v, error = null) }

    fun onPasswordChange(v: String) =
        _estado.update { it.copy(password = v, error = null) }


    /** LOGIN FINAL CORREGIDO */
    fun login(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val usuario = dao.login(email.trim(), password.trim())

                if (usuario != null) {
                    onSuccess(usuario.nombre)
                } else {
                    onError()
                }

            } catch (e: Exception) {
                // Log para debug si algo explota
                Log.e("LoginViewModel", "Error en login: ${e.message}", e)
                onError()
            }
        }
    }


}
