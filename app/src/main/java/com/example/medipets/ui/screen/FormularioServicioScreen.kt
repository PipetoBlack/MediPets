package com.example.medipets.ui.screen

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medipets.ui.components.InputText
import com.example.medipets.viewmodel.HomeViewModel
import com.example.medipets.viewmodel.HomeViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioServicioScreen() {

    // ✅ Crear correctamente el ViewModel usando el Factory
    val context = androidx.compose.ui.platform.LocalContext.current
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(context.applicationContext as Application)
    )

    val estado by viewModel.estado.collectAsState()

    // Listado de regiones de Chile
    val regiones = listOf(
        "Arica y Parinacota",
        "Tarapacá",
        "Antofagasta",
        "Atacama",
        "Coquimbo",
        "Valparaíso",
        "Metropolitana de Santiago",
        "Libertador General Bernardo O’Higgins",
        "Maule",
        "Ñuble",
        "Biobío",
        "La Araucanía",
        "Los Ríos",
        "Los Lagos",
        "Aysén del General Carlos Ibáñez del Campo",
        "Magallanes y de la Antártica Chilena"
    )

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Formulario de Servicio",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        // Campo Nombre
        InputText(
            valor = estado.nombreCliente,
            error = estado.errores.nombreCliente,
            label = "Nombre del cliente",
            onChange = viewModel::onNombreChange
        )

        // Campo Correo
        InputText(
            valor = estado.correoCliente,
            error = estado.errores.correoCliente,
            label = "Correo electrónico",
            onChange = viewModel::onCorreoChange
        )

        // Región (ComboBox)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = estado.region,
                onValueChange = {},
                readOnly = true,
                label = { Text("Región") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                isError = estado.errores.region != null,
                supportingText = {
                    estado.errores.region?.let { errorId -> // 'errorId' es el Int
                        Text(
                            text = stringResource(id = errorId), // ✅ Traduce el ID a texto
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                regiones.forEach { region ->
                    DropdownMenuItem(
                        text = { Text(region) },
                        onClick = {
                            viewModel.onRegionChange(region)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = viewModel::onEnviarFormulario,
            modifier = Modifier.fillMaxWidth(),
            enabled = !estado.errores.tieneErrores()
        ) {
            Text("Enviar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormularioServicioScreenPreview() {
    FormularioServicioScreen()
}
