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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medipets.model.data.config.AppDatabase
import com.example.medipets.model.domain.FormularioCitaMascotaUIState
import com.example.medipets.model.data.repository.FormularioCitaMascotaRepository
import com.example.medipets.ui.components.InputText
import com.example.medipets.viewmodel.FormularioCitaMascotaViewModel
import com.example.medipets.viewmodel.FormularioCitaMascotaViewModelFactory
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioCitaMascotaScreen() {

    val context = LocalContext.current
    val viewModel: FormularioCitaMascotaViewModel = viewModel(
        factory = FormularioCitaMascotaViewModelFactory(
            FormularioCitaMascotaRepository(
                AppDatabase.getDatabase(context.applicationContext as Application).formularioCitaMascotaDao()
            )
        )
    )
    val uiState by viewModel.uiState.collectAsState()

    // --- Lógica del Calendario (DatePicker) ---
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
            // ... (Tus otros InputText para nombre, raza, edad) ...
            InputText(
                valor = uiState.nombreMascota,
                error = uiState.errores.nombreMascota,
                label = "Nombre de la mascota",
                onChange = viewModel::onNombreMascotaChange // Forma más corta de escribirlo
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

            // --- CAMPO DE FECHA MEJORADO (USANDO TU INPUTTEXT ACTUAL) ---
            // Envolvemos tu InputText en un Box que captura el clic.
            Box(modifier = Modifier.clickable { viewModel.onShowDatePicker() }) {
                // Tu InputText se usa aquí, pero deshabilitado a nivel de UI.
                // Sigue mostrando el valor y el error perfectamente.
                InputText(
                    valor = uiState.fecha,
                    error = uiState.errores.fecha,
                    label = "Fecha de la cita",
                    onChange = { }, // Se deja vacío porque el clic lo maneja el Box
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
