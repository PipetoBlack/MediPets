package com.example.medipets.viewmodel

import android.content.Context
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
            val usuario = dao.login(email, password) // O dao.findUser(email)

            if (usuario != null && usuario.password == password) {
                onSuccess(usuario.nombre)
            } else {
                onError()
            }
        }
    }

}
