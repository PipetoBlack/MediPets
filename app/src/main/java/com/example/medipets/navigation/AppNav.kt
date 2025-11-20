package com.example.medipets.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medipets.ui.screen.*

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
                onAgendarClick = { /* Lógica futura para agendar */ },
                onVeterinarioClick = {navController.navigate("veterinario")}
            )
        }

        // Pantalla de ingreso de Veterinario
        composable("veterinario") {
            val context = androidx.compose.ui.platform.LocalContext.current
            val viewModel = com.example.medipets.viewmodel.VeterinarioViewModelFactory(context)
                .create(com.example.medipets.viewmodel.VeterinarioViewModel::class.java)

            VeterinarioProfileScreen(viewModel = viewModel)
        }
    }
}
