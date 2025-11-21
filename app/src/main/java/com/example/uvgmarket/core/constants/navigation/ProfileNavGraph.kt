package com.example.uvgmarket.core.constants.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.uvgmarket.change_password.ChangePasswordScreen
import com.example.uvgmarket.edit_profile.EditProfileScreen
import com.example.uvgmarket.profile.ProfileScreen
import com.example.uvgmarket.profile.MyProfileScreen
import com.example.uvgmarket.profile.repository.UserRepository
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
                navigationActions.navigateToChat()
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
            // YA NO pasamos datos hardcodeados, el ViewModel los carga
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
            onContactSellerClick = {
                navigationActions.navigateToChat()
            }
        )
    }

    composable(route = NavigationDestination.ChatGeneral.route) {
        PantallaChatGeneral(
            onBackClick = { navigationActions.navigateBack() },
            onChatClick = {
                navigationActions.navigateToChat()
            },
            onProfileAvatarClick = {
                navigationActions.navigateToProfile()
            }
        )
    }

    composable(route = NavigationDestination.Chat.route) {
        ChatScreen(
            onBackClick = { navigationActions.navigateBack() }
        )
    }
}