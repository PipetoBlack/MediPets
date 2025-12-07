package com.example.medipets.ui.screen

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.medipets.model.data.entities.MascotaEntity
import com.example.medipets.util.crearImagenUri
import com.example.medipets.viewmodel.MascotaViewModel

@Composable
fun MascotaProfileScreen(
    viewModel: MascotaViewModel
) {
    val estado by viewModel.estado.collectAsState()
    val mascotas by viewModel.mascotas.collectAsState()

    val context = LocalContext.current

    var currentPhotoUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para pedir permiso de cámara
    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = crearImagenUri(context)
            currentPhotoUri = uri
            takePictureLauncher.launch(uri)
        }
    }

    // Launcher para tomar foto
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.onFotoChange(currentPhotoUri.toString())
        } else {
            currentPhotoUri = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Perfil de Paciente / Mascota", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = estado.nombre,
            onValueChange = { viewModel.onNombreChange(it) },
            label = { Text("Nombre de la mascota") },
            modifier = Modifier.fillMaxWidth(),
            isError = estado.errores.nombre != null,
            supportingText = {
                estado.errores.nombre?.let { Text(it) }
            }
        )

        OutlinedTextField(
            value = estado.tipo,
            onValueChange = { viewModel.onTipoChange(it) },
            label = { Text("Tipo de animal (Perro, Gato, etc.)") },
            modifier = Modifier.fillMaxWidth(),
            isError = estado.errores.tipo != null,
            supportingText = {
                estado.errores.tipo?.let { Text(it) }
            }
        )

        OutlinedTextField(
            value = estado.raza,
            onValueChange = { viewModel.onRazaChange(it) },
            label = { Text("Raza") },
            modifier = Modifier.fillMaxWidth(),
            isError = estado.errores.raza != null,
            supportingText = {
                estado.errores.raza?.let { Text(it) }
            }
        )

        OutlinedTextField(
            value = estado.edad,
            onValueChange = { viewModel.onEdadChange(it) },
            label = { Text("Edad (años)") },
            modifier = Modifier.fillMaxWidth(),
            isError = estado.errores.edad != null,
            supportingText = {
                estado.errores.edad?.let { Text(it) }
            }
        )

        Spacer(Modifier.height(12.dp))

        // Foto actual
        estado.fotoUri?.let { uriString ->
            val painter = rememberAsyncImagePainter(model = uriString)
            Image(
                painter = painter,
                contentDescription = "Foto mascota",
                modifier = Modifier
                    .size(120.dp)
                    .padding(8.dp)
            )
        }

        Button(
            onClick = {
                permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tomar foto de la mascota")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { viewModel.guardarMascota() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Paciente")
        }

        Spacer(Modifier.height(16.dp))
        Divider()
        Spacer(Modifier.height(8.dp))

        Text("Pacientes registrados", style = MaterialTheme.typography.titleMedium)

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(mascotas) { mascota: MascotaEntity ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Nombre: ${mascota.nombre}")
                        Text("Tipo: ${mascota.tipo}")
                        Text("Raza: ${mascota.raza}")
                        mascota.edad?.let { Text("Edad: $it años") }
                    }
                }
            }
        }
    }
}