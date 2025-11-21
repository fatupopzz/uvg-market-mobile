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
import androidx.lifecycle.viewmodel.compose.viewModel

fun NavGraphBuilder.marketplaceGraph(navigationActions: NavigationActions) {
    composable(NavigationDestination.Marketplace.route) {
        val viewModel: com.example.uvgmarket.pantallainicio.marketplace.MarketplaceViewModel = viewModel()

        MarketplaceScreen(
            viewModel = viewModel,
            onSearchClick = { /* TODO: Implementar búsqueda */ },
            onEntrepreneurClick = { entrepreneur ->
                // Navegar al perfil del vendedor usando su ID real
                navigationActions.navigateToOtherUserProfile(entrepreneur.id)
            },
            onEntrepreneurStarClick = { entrepreneur ->
                // Manejar click en estrellas
            },
            onFabClick = { navigationActions.navigateToChatGeneral() },
            onProductImageClick = { productId ->
                // CORREGIDO: productId ahora es el ID real del producto de Firebase
                // Ya no es el nombre de la imagen, sino el ID del documento
                navigationActions.navigateToProductDetail(productId, showContactButton = true)
            },
            onProfileAvatarClick = { navigationActions.navigateToProfile() }
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
            onBackClick = { navigationActions.navigateBack() },
            onContactSellerClick = { vendorId ->
                navigationActions.navigateToChat(vendorId)
            },
            showContactButton = showContactButton
        )
    }

    composable(NavigationDestination.AddProduct.route) {
        AgregarProductoScreen(
            onCancelar = { navigationActions.navigateBack() },
            onPublicar = { nombre, descripcion, precio, tieneImagen ->
                // Después de publicar, regresar al marketplace
                navigationActions.navigateBack()
            }
        )
    }

    composable(NavigationDestination.ChatGeneral.route) {
        PantallaChatGeneral(
            onBackClick = { navigationActions.navigateBack() },
            onChatClick = { chatId ->
                // TODO: Implementar navegación al chat específico
            },
            onProfileAvatarClick = { navigationActions.navigateToProfile() }
        )
    }
}