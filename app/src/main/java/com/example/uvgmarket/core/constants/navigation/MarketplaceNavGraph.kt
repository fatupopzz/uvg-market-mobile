package com.example.uvgmarket.core.constants.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.uvgmarket.ordenes_Adr.product_detail.ProductDetailScreen
import com.example.uvgmarket.addProd.AgregarProductoScreen
import com.example.uvgmarket.chat_general.PantallaChatGeneral
import com.example.uvgmarket.pantallainicio.marketplace.MarketplaceScreen
import com.example.uvgmarket.core.navigation.NavigationActions
import com.example.uvgmarket.core.navigation.NavigationDestination

fun NavGraphBuilder.marketplaceGraph(navigationActions: NavigationActions) {
    // Pantalla principal del Marketplace
    composable(NavigationDestination.Marketplace.route) {
        MarketplaceScreen(
            onSearchClick = { /* TODO: Implementar búsqueda */ },
            onEntrepreneurClick = { entrepreneur ->
                val userId = when (entrepreneur.name) {
                    "Hamburguesas kawaii" -> "1"
                    "Accesorios Luna" -> "2"
                    "TechRepair GT" -> "3"
                    else -> "1"
                }
                navigationActions.navigateToOtherUserProfile(userId)
            },
            onEntrepreneurStarClick = { entrepreneur ->
                val userId = when (entrepreneur.name) {
                    "Hamburguesas kawaii" -> "1"
                    "Accesorios Luna" -> "2"
                    "TechRepair GT" -> "3"
                    else -> "1"
                }
                navigationActions.navigateToOtherUserProfile(userId)
            },
            onFabClick = { navigationActions.navigateToChatGeneral() },
            onProductImageClick = { productImageName ->
                navigationActions.navigateToProductDetail(productImageName, showContactButton = true)
            },
            onProfileAvatarClick = { navigationActions.navigateToProfile() }
        )
    }

    // Pantalla de detalle de producto
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
            onBackClick = { navigationActions.navigateBack() },
            onContactSellerClick = { },
            showContactButton = showContactButton
        )
    }

    // Pantalla de agregar producto
    composable(NavigationDestination.AddProduct.route) {
        AgregarProductoScreen(
            onCancelar = { navigationActions.navigateBack() },
            onPublicar = { nombre, descripcion, precio, tieneImagen ->
                navigationActions.navigateBack()
            }
        )
    }

    // Pantalla de chat general
    composable(NavigationDestination.ChatGeneral.route) {
        PantallaChatGeneral(
            onBackClick = { navigationActions.navigateBack() },
            onChatClick = { chatId -> },
            onProfileAvatarClick = { navigationActions.navigateToProfile() }
        )
    }
}