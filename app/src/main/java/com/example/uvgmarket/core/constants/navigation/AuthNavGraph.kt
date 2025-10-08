package com.example.uvgmarket.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.uvgmarket.auth.AuthScreen
import com.example.uvgmarket.presentation.registro.RegistroScreen
import com.example.uvgmarket.presentation.welcome.WelcomeBackScreen

fun NavGraphBuilder.authGraph(navigationActions: NavigationActions) {
    composable(NavigationDestination.Auth.route) {
        AuthScreen(
            onLogin = { navigationActions.navigateToLogin() },
            onRegister = { navigationActions.navigateToRegister() }
        )
    }

    composable(NavigationDestination.Login.route) {
        WelcomeBackScreen(
            onLoginClick = { _, _ -> navigationActions.navigateToMarketplace() },
            onNavigateToRegister = { navigationActions.navigateToRegister() }
        )
    }

    composable(NavigationDestination.Register.route) {
        RegistroScreen(
            onRegistroClick = { _, _, _, _ -> navigationActions.navigateToMarketplace() },
            onNavigateToLogin = { navigationActions.navigateToLogin() }
        )
    }
}