package com.example.medipets.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medipets.viewmodel.PacienteViewModel

@Composable
fun PacienteProfileScreen(
    viewModel: PacienteViewModel
) {
    val ui by viewModel.estado.collectAsState()
    val pacientes by viewModel.pacientes.collectAsState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        Text(
            text = "Perfil de Paciente (Mascota)",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(12.dp))

        // Nombre de la mascota
        OutlinedTextField(
            value = ui.nombreMascota,
            onValueChange = { viewModel.onNombreMascotaChange(it) },
            isError = ui.errores.nombreMascota != null,
            label = { Text("Nombre de la mascota") },
            modifier = Modifier.fillMaxWidth()
        )

        AnimatedVisibility(
            visible = ui.errores.nombreMascota != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = ui.errores.nombreMascota ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(8.dp))

        // Especie
        OutlinedTextField(
            value = ui.especie,
            onValueChange = { viewModel.onEspecieChange(it) },
            isError = ui.errores.especie != null,
            label = { Text("Especie (perro, gato, etc.)") },
            modifier = Modifier.fillMaxWidth()
        )

        AnimatedVisibility(
            visible = ui.errores.especie != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = ui.errores.especie ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(8.dp))

        // Edad
        OutlinedTextField(
            value = ui.edad,
            onValueChange = { viewModel.onEdadChange(it) },
            isError = ui.errores.edad != null,
            label = { Text("Edad (años)") },
            modifier = Modifier.fillMaxWidth()
        )

        AnimatedVisibility(
            visible = ui.errores.edad != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = ui.errores.edad ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(8.dp))

        // Raza (opcional)
        OutlinedTextField(
            value = ui.raza,
            onValueChange = { viewModel.onRazaChange(it) },
            label = { Text("Raza (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { viewModel.onGuardarPaciente() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar paciente")
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Pacientes registrados:",
            style = MaterialTheme.typography.titleMedium
        )

        LazyColumn {
            items(pacientes) { p ->
                Text("- ${p.nombre} (${p.especie}, ${p.edad} años, ${p.raza ?: "Sin raza"})")
            }
        }
    }
}