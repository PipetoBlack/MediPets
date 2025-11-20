package com.example.medipets.ui.screen


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Pantalla inicial de bienvenida que ofrece las opciones de iniciar sesión o registrarse.
 *
 * @param onLoginClick La acción a ejecutar cuando se presiona el botón "Iniciar Sesión".
 * @param onRegisterClick La acción a ejecutar cuando se presiona el botón "Crear Cuenta".
 */
@Composable
fun MenuInicioScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit // <-- La pantalla recibe la orden correctamente.
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Puedes agregar un logo o título aquí si quieres
        Text(
            text = "Bienvenido a MediPet",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(64.dp))

        // Botón para Iniciar Sesión
        Button(
            onClick = onLoginClick, // Este ya te funciona bien.
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar Sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- ¡EL PROBLEMA ESTÁ AQUÍ SEGURAMENTE! ---
        // Botón para Crear Cuenta / Registrarse
        Button(
            // Asegúrate de que el onClick esté llamando a onRegisterClick
            onClick = onRegisterClick, // <-- ¡ESTA ES LA CONEXIÓN CLAVE!
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear Cuenta")
        }
    }
}
