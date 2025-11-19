package com.example.medipets.ui.screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medipets.ui.components.InputText
import com.example.medipets.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onAuthenticated: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    var pwVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        // Usuario
        InputText(
            valor = estado.username,
            error = null,
            label = "Usuario",
            onChange = viewModel::onUsernameChange
        )

        // Contraseña
        OutlinedTextField(
            value = estado.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (pwVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default,
            trailingIcon = {
                TextButton(onClick = { pwVisible = !pwVisible }) {
                    Text(if (pwVisible) "Ocultar" else "Mostrar")
                }
            },
            isError = estado.error != null,
            supportingText = {
                estado.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { viewModel.autenticar(onAuthenticated) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Autenticar")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    LoginScreen(onAuthenticated = {})
}
