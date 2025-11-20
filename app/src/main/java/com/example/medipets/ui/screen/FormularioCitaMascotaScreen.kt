package com.example.medipets.ui.screen

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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

    // Este bloque se ejecuta de forma segura solo una vez cuando la pantalla aparece.
    LaunchedEffect(key1 = Unit) {
        viewModel.eventoFlujo.collectLatest { evento ->
            when (evento) {
                is FormularioCitaMascotaViewModel.Evento.MostrarSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = evento.mensaje,
                        duration = SnackbarDuration.Short
                    )
                }
                is FormularioCitaMascotaViewModel.Evento.NavegarAHome -> {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }
        }
    }

    // lgica del Calendario (DatePicker)
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
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
                TextButton(onClick = { viewModel.onDismissDatePicker() }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Agendar Cita para Mascota") })
        }
    ) { paddingValues ->
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
