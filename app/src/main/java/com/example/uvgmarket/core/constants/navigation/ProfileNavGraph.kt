package com.example.uvgmarket.core.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.uvgmarket.change_password.ChangePasswordScreen
import com.example.uvgmarket.edit_profile.EditProfileScreen
import com.example.uvgmarket.profile.ProfileScreen
import com.example.uvgmarket.profile.ProfileViewModel
import com.example.uvgmarket.profile.MyProfileScreen
import com.example.uvgmarket.profile.repository.UserRepository
import com.example.uvgmarket.chat.ChatScreen
import com.example.uvgmarket.Ordenes_Adr.product_detail.ProductDetailScreen
import com.example.uvgmarket.chat_general.PantallaChatGeneral

fun NavGraphBuilder.profileGraph(navigationActions: NavigationActions) {

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
            onStarClick = {
                // TODO: Implementar sistema de calificación
            }
        )
    }

    // Pantalla de editar perfil
    composable(NavigationDestination.EditProfile.route) {
        val usuario = UserRepository.getCurrentUser()

        EditProfileScreen(
            nombre = usuario.nombre,
            usuario = "hamburguesaskawaii", // TODO: Obtener usuario real
            correo = "hamburguesas@uvg.edu.gt", // TODO: Obtener correo real
            imagenPerfil = usuario.imagenPerfil,
            imagenPortada = usuario.imagenPortada,
            onCancelClick = { navigationActions.navigateBack() },
            onSaveClick = { nombre, usuarioNuevo, correo ->
                // TODO: Guardar cambios del perfil
                navigationActions.navigateBack()
            },
            onChangeProfileImage = {
                // TODO: Implementar cambio de imagen de perfil
            },
            onChangeCoverImage = {
                // TODO: Implementar cambio de imagen de portada
            },
            onChangePassword = { navigationActions.navigateToChangePassword() }
        )
    }

    // Pantalla de cambiar contraseña
    composable(NavigationDestination.ChangePassword.route) {
        ChangePasswordScreen(
            onBackClick = { navigationActions.navigateBack() },
            onConfirmClick = { contrasenaActual, nuevaContrasena, confirmarContrasena ->
                // TODO: Implementar cambio de contraseña
                navigationActions.navigateBack()
            }
        )
    }

    // Pantalla de detalle de producto
    composable(
        route = NavigationDestination.ProductDetail.route,
        arguments = listOf(
            navArgument("productId") { type = NavType.StringType },
            navArgument("showContactButton") { defaultValue = true }
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

    // Pantalla general de chats
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

    // Pantalla de chat
    composable(route = NavigationDestination.Chat.route) {
        ChatScreen(
            onBackClick = { navigationActions.navigateBack() }
        )
    }
}