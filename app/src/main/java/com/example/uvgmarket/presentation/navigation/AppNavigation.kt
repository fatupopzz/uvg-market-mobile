package com.example.uvgmarket.presentation.navigation


sealed class NavigationRoutes(val route: String) {
    object WelcomeBack : NavigationRoutes("welcome_back")
    object Registro : NavigationRoutes("registro")
    object Home : NavigationRoutes("home")
}