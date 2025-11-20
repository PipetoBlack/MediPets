package com.example.medipets.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medipets.viewmodel.VeterinarioViewModel

@Composable
fun VeterinarioProfileScreen(
    viewModel: VeterinarioViewModel
) {
    val veterinarios by viewModel.veterinarios.collectAsState()
    val ui by viewModel.estado.collectAsState()

    // Estados locales del formulario
    var nombre by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Perfil de Veterinario", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(20.dp))

        // --- FORMULARIO ---
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = especialidad,
            onValueChange = { especialidad = it },
            label = { Text("Especialidad") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                viewModel.insertarVeterinario(
                    nombre = nombre,
                    especialidad = especialidad,
                    correo = correo,
                    telefono = telefono
                )

                // limpiar inputs
                nombre = ""
                especialidad = ""
                correo = ""
                telefono = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Veterinario")
        }

        Spacer(modifier = Modifier.height(25.dp))

        Divider()

        Text(
            text = "Veterinarios Registrados",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        // --- LISTA ---
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(veterinarios) { vet ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Nombre: ${vet.nombre}")
                        Text("Especialidad: ${vet.especialidad}")
                        Text("Correo: ${vet.correo}")
                        Text("Teléfono: ${vet.telefono}")
                    }
                }
            }
        }
    }
}