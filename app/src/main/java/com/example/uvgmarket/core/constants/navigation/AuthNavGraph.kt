package com.example.uvgmarket.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.uvgmarket.auth.AuthScreen
import com.example.uvgmarket.presentation.auth.register.RegistroScreen
import com.example.uvgmarket.presentation.auth.login.WelcomeBackScreen

fun NavGraphBuilder.authGraph(navigationActions: NavigationActions) {
    composable(NavigationDestination.Auth.route) {
        AuthScreen(
            onLogin = { navigationActions.navigateToLogin() },
            onRegister = { navigationActions.navigateToRegister() }
        )
    }

    composable(NavigationDestination.Login.route) {
        WelcomeBackScreen(
            onLoginSuccess = {
                // Navegar al marketplace cuando el login sea exitoso
                navigationActions.navigateToMarketplace()
            },
            onNavigateToRegister = {
                navigationActions.navigateToRegister()
            }
        )
    }

    composable(NavigationDestination.Register.route) {
        RegistroScreen(
            onRegistroSuccess = {
                // Navegar al marketplace cuando el registro sea exitoso
                navigationActions.navigateToMarketplace()
            },
            onNavigateToLogin = {
                navigationActions.navigateToLogin()
            }
        )
    }
}