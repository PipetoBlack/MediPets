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
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TextButton
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioCitaMascotaScreen(
    navController: NavController,
    viewModel: FormularioCitaMascotaViewModel
) {

    // 3) Estados
    val uiState by viewModel.uiState.collectAsState()
    val mascotas by viewModel.mascotas.collectAsState()
    val veterinarios by viewModel.veterinarios.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // 4) Eventos del ViewModel
    LaunchedEffect(Unit) {
        viewModel.eventoFlujo.collectLatest { evento ->
            when (evento) {
                is FormularioCitaMascotaViewModel.Evento.MostrarSnackbar ->
                    snackbarHostState.showSnackbar(evento.mensaje)

                is FormularioCitaMascotaViewModel.Evento.NavegarAHome ->
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
            }
        }
    }

    // 5) DatePicker
    val datePickerState = rememberDatePickerState()

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

    // 6) UI principal
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(onBack = { navController.popBackStack() })
                Spacer(modifier = Modifier.width(8.dp))
                Text("Agendar Cita para Mascota", style = MaterialTheme.typography.titleLarge)
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // -------------------- Mascota --------------------
            var expandedMascota by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedMascota,
                onExpandedChange = { expandedMascota = !expandedMascota }
            ) {
                OutlinedTextField(
                    value = uiState.mascotaSeleccionada?.nombre ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Mascota") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expandedMascota)
                    },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedMascota,
                    onDismissRequest = { expandedMascota = false }
                ) {
                    mascotas.forEach { mascota ->
                        DropdownMenuItem(
                            text = { Text(mascota.nombre) },
                            onClick = {
                                viewModel.onMascotaSeleccionada(mascota)
                                expandedMascota = false
                            }
                        )
                    }
                }
            }

            // -------------------- Veterinario --------------------
            var expandedVet by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedVet,
                onExpandedChange = { expandedVet = !expandedVet }
            ) {
                OutlinedTextField(
                    value = uiState.veterinarioSeleccionado?.nombre ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Veterinario") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expandedVet)
                    },
                    modifier = Modifier.fillMaxWidth().menuAnchor()

                )

                ExposedDropdownMenu(
                    expanded = expandedVet,
                    onDismissRequest = { expandedVet = false }
                ) {
                    veterinarios.forEach { vet ->
                        DropdownMenuItem(
                            text = { Text(vet.nombre) },
                            onClick = {
                                viewModel.onVeterinarioSeleccionado(vet)
                                expandedVet = false
                            }
                        )
                    }
                }
            }

            // -------------------- Fecha --------------------
            Box(modifier = Modifier.clickable { viewModel.onShowDatePicker() }) {
                InputText(
                    valor = uiState.fecha,
                    error = uiState.errores.fecha,
                    label = "Fecha de la cita",
                    onChange = {},
                    enabled = true
                )
            }

            // -------------------- Motivo --------------------
            OutlinedTextField(
                value = uiState.motivo,
                onValueChange = viewModel::onMotivoChange,
                label = { Text("Motivo de la consulta") },
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
