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
import com.example.medipets.model.data.repository.MascotaRepository
import com.example.medipets.model.data.repository.VeterinarioRepository
import com.example.medipets.ui.screen.*
import com.example.medipets.viewmodel.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "menu") {

        // MENU
        composable("menu") {
            MenuInicioScreen(
                onLoginClick = { navController.navigate("login") },
                onRegisterClick = { navController.navigate("register") }
            )
        }

        //Login
        composable("login") {
            LoginScreen(
                onLoginSuccess = { nombre ->
                    navController.navigate("home/$nombre") {
                        popUpTo("menu") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }


        // REGISTRO
        composable("register") {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                onLoginClick = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        // HOME CON NOMBRE
        composable("home/{nombre}") { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: "Usuario"

            HomeScreen(
                userName = nombre,
                onLogoutClick = {
                    navController.navigate("menu") {
                        popUpTo("home/{nombre}") { inclusive = true }
                    }
                },
                onAgendarClick = { navController.navigate("CitaMascota") },
                onVeterinarioClick = { navController.navigate("veterinario") },
                onPacienteClick = { navController.navigate("paciente") }
            )
        }

        // VETERINARIO
        composable("veterinario") {
            val context = LocalContext.current
            val viewModel: VeterinarioViewModel = viewModel(
                factory = VeterinarioViewModelFactory(context)
            )
            VeterinarioProfileScreen(viewModel = viewModel, navController = navController)
        }

        // MASCOTA
        composable("paciente") {
            val context = LocalContext.current
            val viewModel: MascotaViewModel = viewModel(
                factory = MascotaViewModelFactory(context)
            )
            MascotaProfileScreen(viewModel = viewModel, navController = navController)
        }

        // FORMULARIO CITA
        composable("CitaMascota") {
            val context = LocalContext.current
            val db = AppDatabase.getDatabase(context)

            val formularioRepo = FormularioCitaMascotaRepository(db.formularioCitaMascotaDao())
            val mascotaRepo = MascotaRepository(db.mascotaDao())
            val veterinarioRepo = VeterinarioRepository(db.veterinarioDao())

            val factory = FormularioCitaMascotaViewModelFactory(
                formularioRepo,
                mascotaRepo,
                veterinarioRepo
            )

            val viewModel: FormularioCitaMascotaViewModel = viewModel(factory = factory)

            FormularioCitaMascotaScreen(
                navController = navController,
                viewModel = viewModel
            )
        }


    }
}
