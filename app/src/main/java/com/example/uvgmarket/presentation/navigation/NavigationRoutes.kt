package com.example.uvgmarket.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uvgmarket.presentation.welcome.WelcomeBackScreen
import com.example.uvgmarket.presentation.registro.RegistroScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.WelcomeBack.route
    ) {
        composable(NavigationRoutes.WelcomeBack.route) {
            WelcomeBackScreen(
                onLoginClick = { usuario, contrasena ->
                    // Lógica de login (datos dummy por ahora)
                    println("Login: $usuario")
                },
                onNavigateToRegister = {
                    navController.navigate(NavigationRoutes.Registro.route)
                }
            )
        }

        composable(NavigationRoutes.Registro.route) {
            RegistroScreen(
                onRegistroClick = { nombre, usuario, correo, contrasena ->
                    // Lógica de registro (datos dummy por ahora)
                    println("Registro: $nombre, $usuario, $correo")
                },
                onNavigateToLogin = {
                    navController.navigateUp()
                }
            )
        }
    }
}