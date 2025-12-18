package com.example.medipets.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medipets.model.data.config.AppDatabase
import com.example.medipets.model.data.repository.UsuarioRepository
import com.example.medipets.viewmodel.RegistroViewModel
import com.example.medipets.viewmodel.RegistroViewModelFactory

@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val context = LocalContext.current

    // Crear la BD + DAO + Repo + ViewModel
    val db = remember { AppDatabase.getDatabase(context) }
    val dao = db.usuarioDao()
    val repo = UsuarioRepository(dao)

    val viewModel: RegistroViewModel =
        viewModel(factory = RegistroViewModelFactory(repo))

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var localError by remember { mutableStateOf("") }

    // Si el registro se completó → navegar al login
    if (viewModel.registroExitoso) {
        Toast.makeText(context, "Registro exitoso", Toast.LENGTH_LONG).show()
        onLoginClick()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre completo") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = null
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar contraseña") },
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = null
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                localError = ""

                if (name.isBlank()) {
                    localError = "El nombre no puede estar vacío."
                    return@Button
                }
                if (!email.contains("@") || !email.contains(".")) {
                    localError = "Correo inválido."
                    return@Button
                }
                if (password.length < 6) {
                    localError = "La contraseña debe tener al menos 6 caracteres."
                    return@Button
                }
                if (password != confirmPassword) {
                    localError = "Las contraseñas no coinciden."
                    return@Button
                }

                viewModel.registrar(name, email, password)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }

        if (localError.isNotEmpty()) {
            Text(localError, color = MaterialTheme.colorScheme.error)
        }
        if (viewModel.mensajeError != null) {
            Text(viewModel.mensajeError!!, color = MaterialTheme.colorScheme.error)
        }
    }
}
