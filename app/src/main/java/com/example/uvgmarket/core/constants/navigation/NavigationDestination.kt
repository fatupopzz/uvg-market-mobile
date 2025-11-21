package com.example.uvgmarket.core.navigation

sealed class NavigationDestination(val route: String) {
    data object Auth : NavigationDestination("auth")
    data object Login : NavigationDestination("login")
    data object Register : NavigationDestination("register")
    data object Marketplace : NavigationDestination("marketplace")
    data object ProductDetail : NavigationDestination("product_detail/{productId}/{showContactButton}") {
        fun createRoute(productId: String, showContactButton: Boolean = true): String {
            return "product_detail/$productId/$showContactButton"
        }
    }
    data object Profile : NavigationDestination("profile")
    data object OtherUserProfile : NavigationDestination("other_user_profile/{userId}") {
        fun createRoute(userId: String): String {
            return "other_user_profile/$userId"
        }
    }
    data object EditProfile : NavigationDestination("edit_profile")
    data object ChangePassword : NavigationDestination("change_password")
    data object AddProduct : NavigationDestination("add_product")
    data object ChatGeneral : NavigationDestination("chat_general")
    data object Chat : NavigationDestination("chat/{userId}") {
        fun createRoute(userId: String): String {
            return "chat/$userId"
        }
    }
}