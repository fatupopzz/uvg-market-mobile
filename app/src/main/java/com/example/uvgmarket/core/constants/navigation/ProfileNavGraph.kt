package com.example.uvgmarket.core.constants.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.uvgmarket.change_password.ChangePasswordScreen
import com.example.uvgmarket.edit_profile.EditProfileScreen
import com.example.uvgmarket.profile.ProfileScreen
import com.example.uvgmarket.profile.MyProfileScreen
import com.example.uvgmarket.chat.ChatScreen
import com.example.uvgmarket.ordenes_Adr.product_detail.ProductDetailScreen
import com.example.uvgmarket.chat_general.PantallaChatGeneral
import com.example.uvgmarket.data.repository.AuthRepository
import com.example.uvgmarket.core.navigation.NavigationActions
import com.example.uvgmarket.core.navigation.NavigationDestination

fun NavGraphBuilder.profileGraph(navigationActions: NavigationActions) {

    val authRepository = AuthRepository()

    // Pantalla de perfil propio
    composable(NavigationDestination.Profile.route) {
        MyProfileScreen(
            onBackClick = { navigationActions.navigateBack() },
            onProductoClick = { productId ->
                navigationActions.navigateToProductDetail(productId, showContactButton = false)
            },
            onAddProductClick = { navigationActions.navigateToAddProduct() },
            onEditClick = { navigationActions.navigateToEditProfile() }
        )
    }

    // Pantalla de perfil de otro usuario
    composable(
        route = NavigationDestination.OtherUserProfile.route,
        arguments = listOf(
            navArgument("userId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val userId = backStackEntry.arguments?.getString("userId") ?: ""

        ProfileScreen(
            userId = userId,
            isOwnProfile = false,
            onBackClick = { navigationActions.navigateBack() },
            onChatClick = { clickedUserId ->
                navigationActions.navigateToChat(clickedUserId)
            },
            onProductoClick = { productId ->
                navigationActions.navigateToProductDetail(productId, showContactButton = true)
            },
            onFloatingActionClick = { },
            onStarClick = { }
        )
    }

    // Pantalla de editar perfil
    composable(NavigationDestination.EditProfile.route) {
        EditProfileScreen(
            onCancelClick = { navigationActions.navigateBack() },
            onSaveSuccess = {
                navigationActions.navigateBack()
            },
            onChangePassword = { navigationActions.navigateToChangePassword() },
            onLogout = {
                authRepository.logout()
                navigationActions.navigateToAuth()
            }
        )
    }

    composable(NavigationDestination.ChangePassword.route) {
        ChangePasswordScreen(
            onBackClick = { navigationActions.navigateBack() },
            onPasswordChanged = {
                navigationActions.navigateBack()
            }
        )
    }

    composable(
        route = NavigationDestination.ProductDetail.route,
        arguments = listOf(
            navArgument("productId") { type = NavType.StringType },
            navArgument("showContactButton") {
                type = NavType.BoolType
                defaultValue = true
            }
        )
    ) { backStackEntry ->
        val productId = backStackEntry.arguments?.getString("productId") ?: ""
        val showContactButton = backStackEntry.arguments?.getBoolean("showContactButton") ?: true

        ProductDetailScreen(
            productId = productId,
            showContactButton = showContactButton,
            onBackClick = { navigationActions.navigateBack() },
            onContactSellerClick = { vendorId ->
                navigationActions.navigateToChat(vendorId)
            }
        )
    }

    composable(route = NavigationDestination.ChatGeneral.route) {
        PantallaChatGeneral(
            onBackClick = { navigationActions.navigateBack() },
            onChatClick = { chatId ->
                // TODO: Necesitarás extraer el userId del chat
                // Por ahora, navega al chat con un userId de ejemplo
                navigationActions.navigateToChat(chatId)
            },
            onProfileAvatarClick = {
                navigationActions.navigateToProfile()
            }
        )
    }

    // RUTA DEL CHAT - CORREGIDA
    composable(
        route = NavigationDestination.Chat.route,
        arguments = listOf(
            navArgument("userId") {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val userId = backStackEntry.arguments?.getString("userId") ?: ""

        ChatScreen(
            otherUserId = userId,
            onBackClick = { navigationActions.navigateBack() }
        )
    }
}