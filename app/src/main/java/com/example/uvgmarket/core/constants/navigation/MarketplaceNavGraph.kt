package com.example.uvgmarket.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.uvgmarket.Ordenes_Adr.product_detail.ProductDetailScreen
import com.example.uvgmarket.addProd.AgregarProductoScreen
import com.example.uvgmarket.chat_general.PantallaChatGeneral
import com.example.uvgmarket.pantallainicio.marketplace.MarketplaceScreen

fun NavGraphBuilder.marketplaceGraph(navigationActions: NavigationActions) {
    // Pantalla principal del Marketplace
    composable(NavigationDestination.Marketplace.route) {
        MarketplaceScreen(
            onSearchClick = { /* TODO: Implementar búsqueda */ },
            onEntrepreneurClick = { entrepreneur ->
                // Navegar al perfil del emprendedor cuando se hace click en la card
                navigationActions.navigateToOtherUserProfile(entrepreneur.name)
            },
            onEntrepreneurStarClick = { entrepreneur ->
                // También navegar al perfil cuando se hace click en las estrellas
                navigationActions.navigateToOtherUserProfile(entrepreneur.name)
            },
            onFabClick = { navigationActions.navigateToChatGeneral() },
            onProductImageClick = { productImage ->
                // Navegar al detalle del producto con botón de contactar
                navigationActions.navigateToProductDetail(productImage, showContactButton = true)
            },
            onProfileAvatarClick = { navigationActions.navigateToProfile() }
        )
    }

    // Pantalla de detalle de producto con argumentos
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
        val showContactButton = backStackEntry.arguments?.getBoolean("showContactButton") ?: true

        ProductDetailScreen(
            onBackClick = { navigationActions.navigateBack() },
            onContactSellerClick = {
                // TODO: Implementar chat individual con vendedor
                // Por ahora no hace nada
            },
            showContactButton = showContactButton
        )
    }

    // Pantalla de agregar producto
    composable(NavigationDestination.AddProduct.route) {
        AgregarProductoScreen(
            onCancelar = { navigationActions.navigateBack() },
            onPublicar = { nombre, descripcion, precio, tieneImagen ->
                // TODO: Guardar producto
                // Por ahora solo navega de regreso
                navigationActions.navigateBack()
            }
        )
    }

    // Pantalla de chat general
    composable(NavigationDestination.ChatGeneral.route) {
        PantallaChatGeneral(
            onBackClick = { navigationActions.navigateBack() },
            onChatClick = { chatId ->
                // TODO: Navegar a chat individual cuando esté implementado
                // Por ahora no hace nada
            },
            onProfileAvatarClick = { navigationActions.navigateToProfile() }
        )
    }
}