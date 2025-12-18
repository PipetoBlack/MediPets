package com.example.medipets.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medipets.model.domain.WeatherUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherTopBar(
    state: WeatherUIState,
    onRefreshClick: () -> Unit,
    extraContent: @Composable () -> Unit
) {
    TopAppBar(
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // ✅ Saludo + botón salir
                extraContent()

                // ✅ Clima abajo con ícono y texto
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    when {
                        state.isLoading -> Text("Cargando clima…")
                        state.errorMessage != null -> Text("Error: ${state.errorMessage}")
                        state.city.isNotBlank() -> {
                            Icon(
                                imageVector = Icons.Default.Refresh, // temporal, luego cambiamos por ícono de clima real
                                contentDescription = "Clima",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text("${state.city}: ${state.temperature} • ${state.condition}")
                        }
                        else -> Text("Clima no disponible")
                    }
                }
            }
        },
        actions = {
            IconButton(onClick = onRefreshClick) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Actualizar clima"
                )
            }
        }
    )
}
