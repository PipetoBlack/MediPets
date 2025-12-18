package com.example.medipets.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
            Column {
                when {
                    state.isLoading -> Text("Cargando clima…")
                    state.errorMessage != null -> Text("Error: ${state.errorMessage}")
                    state.city.isNotBlank() ->
                        Text("${state.city}: ${state.temperature} • ${state.condition}")
                    else -> Text("Clima no disponible")
                }

                extraContent()
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
