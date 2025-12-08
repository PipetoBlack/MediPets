package com.example.medipets.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medipets.ui.components.BackButton
import com.example.medipets.ui.components.InputText
import com.example.medipets.viewmodel.FormularioCitaMascotaViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioCitaMascotaScreen(
    navController: NavController,
    viewModel: FormularioCitaMascotaViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Eventos del ViewModel
    LaunchedEffect(Unit) {
        viewModel.eventoFlujo.collectLatest { evento ->
            when (evento) {
                is FormularioCitaMascotaViewModel.Evento.MostrarSnackbar ->
                    snackbarHostState.showSnackbar(evento.mensaje)

                is FormularioCitaMascotaViewModel.Evento.NavegarAHome -> {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }
        }
    }

    // DatePicker
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())

    if (uiState.mostrarDatePicker) {
        DatePickerDialog(
            onDismissRequest = { viewModel.onDismissDatePicker() },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.onDateSelected(it)
                    }
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onDismissDatePicker() }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // =======================  SCAFFOLD  ===========================
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(onBack = { navController.popBackStack() })

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Agendar Cita para Mascota",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    ) { paddingValues ->

        // =======================  CONTENIDO  ===========================
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            InputText(
                valor = uiState.nombreMascota,
                error = uiState.errores.nombreMascota,
                label = "Nombre de la mascota",
                onChange = viewModel::onNombreMascotaChange
            )

            InputText(
                valor = uiState.raza,
                error = uiState.errores.raza,
                label = "Raza",
                onChange = viewModel::onRazaChange
            )

            InputText(
                valor = uiState.edad,
                error = uiState.errores.edad,
                label = "Edad",
                onChange = viewModel::onEdadChange
            )

            // Fecha → disabled y clickable
            Box(modifier = Modifier.clickable { viewModel.onShowDatePicker() }) {
                InputText(
                    valor = uiState.fecha,
                    error = uiState.errores.fecha,
                    label = "Fecha de la cita",
                    onChange = { },
                    enabled = false
                )
            }

            OutlinedTextField(
                value = uiState.motivo,
                onValueChange = viewModel::onMotivoChange,
                label = { Text("Motivo de la consulta") },
                isError = uiState.errores.motivo != null,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )

            // Empuja el botón al final
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = viewModel::guardarCita,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("GUARDAR CITA")
            }
        }
    }
}
