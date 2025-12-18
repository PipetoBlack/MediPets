package com.example.medipets.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medipets.model.domain.WeatherUIState
import com.example.medipets.ui.components.WeatherTopBar
import com.example.medipets.viewmodel.WeatherViewModel
import com.example.medipets.viewmodel.WeatherViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String,
    onLogoutClick: () -> Unit,
    onAgendarClick: () -> Unit,
    onVeterinarioClick: () -> Unit,
    onPacienteClick: () -> Unit
) {
    // ✅ 1. Instanciamos el ViewModel del clima
    val weatherViewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModelFactory()
    )

    // ✅ 2. Obtenemos el estado del clima
    val weatherState = weatherViewModel.uiState

    // ✅ 3. Cargar clima al entrar a la pantalla
    LaunchedEffect(Unit) {
        weatherViewModel.loadWeather("Santiago")
    }

    Scaffold(
        topBar = {
            // ✅ 4. Integramos WeatherTopBar + tu saludo + botón salir
            WeatherTopBar(
                state = weatherState,
                onRefreshClick = { weatherViewModel.loadWeather("Santiago") },
                extraContent = {
                    // ✅ Aquí va tu TopAppBar original, sin tocarlo
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Hola, $userName",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(Modifier.width(12.dp))

                        TextButton(onClick = onLogoutClick) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ExitToApp,
                                    contentDescription = "Cerrar sesión"
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(text = "Salir")
                            }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onAgendarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(text = "Agendar Cita", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                onClick = onVeterinarioClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(text = "Registro Veterinario", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                onClick = onPacienteClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(text = "Perfil Mascota", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
