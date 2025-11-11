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

    fun navigateToProductDetail(productId: String, showContactButton: Boolean = true) {
        navController.navigate(
            NavigationDestination.ProductDetail.createRoute(productId, showContactButton)
        )
    }

    fun navigateToProfile() {
        navController.navigate(NavigationDestination.Profile.route)
    }

    fun navigateToOtherUserProfile(userId: String) {
        navController.navigate(
            NavigationDestination.OtherUserProfile.createRoute(userId)
        )
    }

    fun navigateToEditProfile() {
        navController.navigate(NavigationDestination.EditProfile.route)
    }

    fun navigateToChangePassword() {
        navController.navigate(NavigationDestination.ChangePassword.route)
    }

    fun navigateToAddProduct() {
        navController.navigate(NavigationDestination.AddProduct.route)
    }

    fun navigateToChatGeneral() {
        navController.navigate(NavigationDestination.ChatGeneral.route)
    }

    fun navigateToChat() {
        navController.navigate(NavigationDestination.Chat.route)
    }

    fun navigateBack() {
        navController.navigateUp()
    }
}