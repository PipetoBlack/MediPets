package com.example.medipets.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medipets.model.data.config.AppDatabase
import com.example.medipets.model.data.repository.FormularioCitaMascotaRepository
import com.example.medipets.ui.screen.*
import com.example.medipets.viewmodel.FormularioCitaMascotaViewModel
import com.example.medipets.viewmodel.FormularioCitaMascotaViewModelFactory
import com.example.medipets.viewmodel.VeterinarioViewModel
import com.example.medipets.viewmodel.VeterinarioViewModelFactory
import com.example.medipets.viewmodel.MascotaViewModel
import com.example.medipets.viewmodel.MascotaViewModelFactory
import com.example.medipets.ui.screen.MascotaProfileScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "menu") {

        // Pantalla del Menú de Inicio
        composable("menu") {
            MenuInicioScreen(
                onLoginClick = { navController.navigate("login") },
                onRegisterClick = { navController.navigate("register") }
            )
        }

        // Pantalla de Login
        composable("login") {
            LoginScreen(
                onLoginClick = {
                    navController.navigate("home") { popUpTo("menu") { inclusive = true } }
                },
                onNavigateToRegister = {
                    navController.navigate("register") // Esta es la navegación clave
                }
            )
        }

        // Pantalla de Registro
        composable("register") {
            RegisterScreen(
                onBackClick = { navController.popBackStack() }, // Volver a la pantalla anterior
                onLoginClick = {
                    navController.navigate("login") { popUpTo("register") { inclusive = true } }
                }
            )
        }

        // Pantalla de Home
        composable("home") {
            HomeScreen(
                userName = "Felipe",
                onLogoutClick = {
                    navController.navigate("menu") { popUpTo("home") { inclusive = true } }
                },
                onAgendarClick = { navController.navigate("CitaMascota") },
                onVeterinarioClick = { navController.navigate("veterinario") },
                onPacienteClick = { navController.navigate("paciente") }
            )
        }

        // Pantalla de ingreso de Veterinario
        composable("veterinario") {
            val context = LocalContext.current
            val viewModel: VeterinarioViewModel = viewModel(
                factory = VeterinarioViewModelFactory(context)
            )
            VeterinarioProfileScreen(viewModel = viewModel)
        }

        // Pantalla de la mascota
        composable("paciente") {
            val context = LocalContext.current
            val viewModel: MascotaViewModel = viewModel(
                factory = MascotaViewModelFactory(context)
            )
            MascotaProfileScreen(viewModel = viewModel)
        }

        // Pantalla de ingreso de Formulario cita
        composable("CitaMascota") {
            val application = LocalContext.current.applicationContext as Application
            val dao = AppDatabase.getDatabase(application).formularioCitaMascotaDao()
            val repository = FormularioCitaMascotaRepository(dao)
            val factory = FormularioCitaMascotaViewModelFactory(repository)
            val viewModel: FormularioCitaMascotaViewModel = viewModel(factory = factory)

            FormularioCitaMascotaScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
