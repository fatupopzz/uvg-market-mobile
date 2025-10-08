package com.example.uvgmarket.core.navigation

sealed class NavigationDestination(val route: String) {
    data object Auth : NavigationDestination("auth")
    data object Login : NavigationDestination("login")
    data object Register : NavigationDestination("register")
    data object Marketplace : NavigationDestination("marketplace")
    data object ProductDetail : NavigationDestination("product_detail")
    data object Profile : NavigationDestination("profile")
}