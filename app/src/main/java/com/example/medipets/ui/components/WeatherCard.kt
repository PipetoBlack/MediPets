package com.example.medipets.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medipets.model.domain.WeatherUIState

@Composable
fun WeatherCard(state: WeatherUIState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                state.isLoading -> Text("Cargando clima…")
                state.errorMessage != null -> Text("Error: ${state.errorMessage}")
                state.city.isNotBlank() -> {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = "Clima",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text("${state.city}", style = MaterialTheme.typography.titleMedium)
                    Text("${state.temperature} °C • ${state.condition}", style = MaterialTheme.typography.bodyMedium)
                }
                else -> Text("Clima no disponible")
            }
        }
    }
}
