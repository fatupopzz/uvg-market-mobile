package com.example.uvgmarket.core.navigation

import androidx.navigation.NavHostController

class NavigationActions(private val navController: NavHostController) {

    fun navigateToAuth() {
        navController.navigate(NavigationDestination.Auth.route)
    }

    fun navigateToLogin() {
        navController.navigate(NavigationDestination.Login.route)
    }

    fun navigateToRegister() {
        navController.navigate(NavigationDestination.Register.route)
    }

    fun navigateToMarketplace() {
        navController.navigate(NavigationDestination.Marketplace.route) {
            popUpTo(NavigationDestination.Auth.route) { inclusive = true }
        }
    }

    fun navigateToProductDetail() {
        navController.navigate(NavigationDestination.ProductDetail.route)
    }

    fun navigateToProfile() {
        navController.navigate(NavigationDestination.Profile.route)
    }

    fun navigateBack() {
        navController.navigateUp()
    }
}