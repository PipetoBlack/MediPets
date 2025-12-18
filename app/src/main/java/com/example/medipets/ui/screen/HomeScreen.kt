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
import com.example.medipets.ui.components.WeatherCard
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
    val weatherViewModel: WeatherViewModel = viewModel(factory = WeatherViewModelFactory())
    val weatherState = weatherViewModel.uiState

    LaunchedEffect(Unit) {
        weatherViewModel.loadWeather("Santiago")
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ✅ Saludo + botón salir
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hola, $userName",
                    style = MaterialTheme.typography.titleMedium
                )
                TextButton(onClick = onLogoutClick) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                        Spacer(Modifier.width(4.dp))
                        Text("Salir")
                    }
                }
            }

            // ✅ Clima como card
            WeatherCard(state = weatherState)

            // ✅ Botones
            Button(
                onClick = onAgendarClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agendar Cita", style = MaterialTheme.typography.titleMedium)
            }

            Button(
                onClick = onVeterinarioClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registro Veterinario", style = MaterialTheme.typography.titleMedium)
            }

            Button(
                onClick = onPacienteClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Perfil Mascota", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

