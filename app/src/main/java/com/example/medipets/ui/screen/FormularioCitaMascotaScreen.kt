package com.example.medipets.ui.screen

// --- IMPORTS NECESARIOS ---
// Estos imports son nuevos y necesarios para la conexión.
import android.app.Application
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medipets.model.data.config.AppDatabase
import com.example.medipets.model.data.repository.FormularioCitaMascotaRepository
import com.example.medipets.viewmodel.FormularioCitaMascotaViewModel
import com.example.medipets.viewmodel.FormularioCitaMascotaViewModelFactory
import androidx.compose.ui.platform.LocalContext
// --- IMPORTS QUE YA TENÍAS ---
import com.example.medipets.ui.components.InputText
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioCitaMascotaScreen() {

    // --- 1. CREACIÓN DEL VIEWMODEL ---
    // Aquí se crea la instancia del "cerebro" de la pantalla.
    val context = LocalContext.current
    val viewModel: FormularioCitaMascotaViewModel = viewModel(
        factory = FormularioCitaMascotaViewModelFactory(
            FormularioCitaMascotaRepository(
                AppDatabase.getDatabase(context.applicationContext as Application).formularioCitaMascotaDao()
            )
        )
    )

    // --- 2. OBSERVACIÓN DEL ESTADO ---
    // `uiState` ahora es una "foto" viva del estado del ViewModel.
    val uiState by viewModel.uiState.collectAsState()

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

            // --- 3. CONEXIÓN DE LOS CAMPOS DE TEXTO ---
            // Cada InputText ahora está vinculado al uiState y al ViewModel.
            InputText(
                valor = uiState.nombreMascota,
                error = uiState.errores.nombreMascota,
                label = "Nombre de la mascota",
                onChange = { viewModel.onNombreMascotaChange(it) }
            )

            InputText(
                valor = uiState.raza,
                error = uiState.errores.raza,
                label = "Raza",
                onChange = { viewModel.onRazaChange(it) }
            )

            InputText(
                valor = uiState.edad,
                error = uiState.errores.edad,
                label = "Edad",
                onChange = { viewModel.onEdadChange(it) }
            )

            InputText(
                valor = uiState.fecha,
                error = uiState.errores.fecha,
                label = "Fecha de la cita (dd/mm/aaaa)",
                onChange = { viewModel.onFechaChange(it) }
            )

            // Este campo usa OutlinedTextField para 'maxLines', también conectado.
            OutlinedTextField(
                value = uiState.motivo,
                onValueChange = { viewModel.onMotivoChange(it) },
                label = { Text("Motivo de la consulta") },
                isError = uiState.errores.motivo != null,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )

            Spacer(modifier = Modifier.weight(1f))

            // --- 4. CONEXIÓN DEL BOTÓN ---
            Button(
                onClick = { viewModel.guardarCita() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("GUARDAR CITA")
            }
        }
    }
}
