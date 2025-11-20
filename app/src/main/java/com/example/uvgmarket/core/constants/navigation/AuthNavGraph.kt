package com.example.uvgmarket.core.constants.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.uvgmarket.auth.AuthScreen
import com.example.uvgmarket.presentation.auth.register.RegistroScreen
import com.example.uvgmarket.presentation.auth.login.WelcomeBackScreen
import com.example.uvgmarket.core.navigation.NavigationActions
import com.example.uvgmarket.core.navigation.NavigationDestination

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
                // Ya no se usa, el diálogo maneja la navegación
            },
            onNavigateToLogin = {
                navigationActions.navigateToLogin()
            }
        )
    }
}