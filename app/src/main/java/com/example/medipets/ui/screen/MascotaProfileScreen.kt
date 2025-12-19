package com.example.medipets.ui.screen

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.medipets.model.data.entities.MascotaEntity
import com.example.medipets.util.crearImagenUri
import com.example.medipets.viewmodel.MascotaViewModel
import com.example.medipets.ui.components.BackButton
import androidx.navigation.NavHostController
import androidx.compose.material3.Divider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotaProfileScreen(
    viewModel: MascotaViewModel,
    navController: NavHostController
) {
    val estado by viewModel.estado.collectAsState()
    val mascotas by viewModel.mascotas.collectAsState()
    val razasDinamicas by viewModel.razasBackend.collectAsState() // Razas de tu API
    val estaCargando by viewModel.estaCargando.collectAsState()

    val context = LocalContext.current
    var currentPhotoUri by remember { mutableStateOf<Uri?>(null) }

    // Control de menús
    val tiposMascota = listOf("Perro", "Gato")
    var expandidoTipo by remember { mutableStateOf(false) }
    var expandidoRaza by remember { mutableStateOf(false) }

    // Launcher para tomar la foto
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) viewModel.onFotoChange(currentPhotoUri?.toString())
    }

    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = crearImagenUri(context)
            currentPhotoUri = uri
            takePictureLauncher.launch(uri)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de Paciente") },
                navigationIcon = { BackButton(onBack = { navController.popBackStack() }) }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- FOTO GRANDE ---
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(8.dp))

                    // --- VISOR DE FOTO ---
                    if (estado.fotoUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(model = estado.fotoUri),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            // Si hay error, podemos teñir el borde de rojo
                            color = if (estado.errores.foto != null)
                                MaterialTheme.colorScheme.errorContainer
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(if (estado.errores.foto != null) "¡FOTO REQUERIDA!" else "Sin foto")
                            }
                        }
                    }

                    // --- MENSAJE DE ERROR DE FOTO ---
                    if (estado.errores.foto != null) {
                        Text(
                            text = estado.errores.foto!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // --- BOTÓN CÁMARA ---
                    Button(
                        onClick = { permisoCamaraLauncher.launch(Manifest.permission.CAMERA) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (estado.errores.foto != null)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Tomar Foto")
                    }

                    Spacer(Modifier.height(16.dp))
                }
            }

            // --- FORMULARIO CON DROPDOWNS ---
            item {
                OutlinedTextField(
                    value = estado.nombre,
                    onValueChange = { viewModel.onNombreChange(it) },
                    label = { Text("Nombre de la mascota") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = estado.errores.nombre != null
                )

                // DROPDOWN TIPO
                ExposedDropdownMenuBox(
                    expanded = expandidoTipo,
                    onExpandedChange = { expandidoTipo = !expandidoTipo }
                ) {
                    OutlinedTextField(
                        value = estado.tipo,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo (Perro, Gato)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandidoTipo) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandidoTipo, onDismissRequest = { expandidoTipo = false }) {
                        tiposMascota.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    viewModel.onTipoChange(opcion)
                                    viewModel.onRazaChange("") // Limpiamos la anterior
                                    viewModel.cargarRazas(opcion) // ¡AQUÍ PEDIMOS AL BACKEND!
                                    expandidoTipo = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // DROPDOWN RAZA (Dinámico del Backend)
                ExposedDropdownMenuBox(
                    expanded = expandidoRaza,
                    onExpandedChange = { if (estado.tipo.isNotEmpty()) expandidoRaza = !expandidoRaza }
                ) {
                    OutlinedTextField(
                        value = estado.raza,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Raza") },
                        placeholder = { Text("Selecciona tipo primero") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandidoRaza) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        enabled = estado.tipo.isNotEmpty() && !estaCargando
                    )
                    ExposedDropdownMenu(expanded = expandidoRaza, onDismissRequest = { expandidoRaza = false }, modifier = Modifier.heightIn(max = 280.dp)) {
                        razasDinamicas.forEach { r ->
                            DropdownMenuItem(
                                text = { Text(r) },
                                onClick = {
                                    viewModel.onRazaChange(r)
                                    expandidoRaza = false
                                }
                            )
                        }
                    }
                }

                Row(Modifier.fillMaxWidth()) {
                    OutlinedTextField(value = estado.edadAnios, onValueChange = { viewModel.onEdadAniosChange(it) }, label = { Text("Años") }, modifier = Modifier.weight(1f))
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(value = estado.edadMeses, onValueChange = { viewModel.onEdadMesesChange(it) }, label = { Text("Meses") }, modifier = Modifier.weight(1f))
                }

                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = { viewModel.guardarMascota() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !estaCargando
                ) {
                    if (estaCargando) {
                        // CORRECCIÓN AQUÍ: El tamaño va dentro del Modifier
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Guardando...")
                    } else {
                        Text("Guardar Paciente")
                    }
                }

                Spacer(Modifier.height(32.dp))
                Divider()
                Text("Pacientes Registrados", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
            }

            // --- LISTA ---
            items(mascotas) { mascota ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        if (mascota.fotoUri != null) {
                            Image(rememberAsyncImagePainter(mascota.fotoUri), null, Modifier.size(50.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(mascota.nombre, style = MaterialTheme.typography.titleSmall)
                            Text("${mascota.tipo} - ${mascota.raza}")
                        }
                    }
                }
            }
        }
    }
}