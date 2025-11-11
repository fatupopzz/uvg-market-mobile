package com.example.uvgmarket.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uvgmarket.presentation.auth.login.WelcomeBackScreen
import com.example.uvgmarket.presentation.auth.register.RegistroScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.WelcomeBack.route
    ) {
        composable(NavigationRoutes.WelcomeBack.route) {
            WelcomeBackScreen(
                onLoginSuccess = {

                },
                onNavigateToRegister = {
                    navController.navigate(NavigationRoutes.Registro.route)
                }
            )
        }

        composable(NavigationRoutes.Registro.route) {
            RegistroScreen(
                onRegistroSuccess = {
                },
                onNavigateToLogin = {
                    navController.navigateUp()
                }
            )
        }
    }
}