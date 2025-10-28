package com.example.uvgmarket.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.uvgmarket.change_password.ChangePasswordScreen
import com.example.uvgmarket.edit_profile.EditProfileScreen
import com.example.uvgmarket.profile.ProfileScreen
import com.example.uvgmarket.profile.repository.UserRepository

fun NavGraphBuilder.profileGraph(navigationActions: NavigationActions) {

    // Pantalla de perfil propio
    composable(NavigationDestination.Profile.route) {
        val currentUser = UserRepository.getCurrentUser()
        val productos = UserRepository.getCurrentUserProducts()

        ProfileScreen(
            usuario = currentUser,
            productos = productos,
            onBackClick = { navigationActions.navigateBack() },
            onChatClick = {
                // No se muestra en perfil propio
            },
            onProductoClick = { productId ->
                // Ver producto propio sin botón de contactar
                navigationActions.navigateToProductDetail(productId, showContactButton = false)
            },
            onFloatingActionClick = { navigationActions.navigateToAddProduct() },
            onDeleteProductClick = { productId ->
                // TODO: Implementar eliminación de producto
            },
            onStarClick = {
                // Las estrellas no son clickeables en perfil propio
            },
            showFloatingActionButton = true,
            showChatButton = false,
            showEditButton = true,
            showDeleteButton = true,
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

        // Obtener datos del usuario específico
        val usuario = UserRepository.getUserById(userId)
        val productos = UserRepository.getProductsByUserId(userId)

        if (usuario != null) {
            ProfileScreen(
                usuario = usuario,
                productos = productos,
                onBackClick = { navigationActions.navigateBack() },
                onChatClick = { clickedUserId ->
                    // TODO: Implementar chat individual con el usuario
                    // Por ahora no hace nada
                },
                onProductoClick = { productId ->
                    // Ver producto de otro usuario con botón de contactar
                    navigationActions.navigateToProductDetail(productId, showContactButton = true)
                },
                onFloatingActionClick = { },
                onDeleteProductClick = { },
                onStarClick = {
                    // TODO: Implementar sistema de calificación
                    // Por ahora no hace nada
                },
                showFloatingActionButton = false,
                showChatButton = true,
                showEditButton = false,
                showDeleteButton = false
            )
        } else {
            // Si no se encuentra el usuario, navegar de regreso
            navigationActions.navigateBack()
        }
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
}