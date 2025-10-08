package com.example.uvgmarket.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.uvgmarket.Ordenes_Adr.product_detail.ProductDetailScreen
import com.example.uvgmarket.pantallainicio.marketplace.MarketplaceScreen

fun NavGraphBuilder.marketplaceGraph(navigationActions: NavigationActions) {
    composable(NavigationDestination.Marketplace.route) {
        MarketplaceScreen(
            onMenuClick = { /* TODO */ },
            onSearchClick = { /* TODO */ },
            onEntrepreneurClick = { navigationActions.navigateToProfile() },
            onFabClick = { /* TODO */ },
            onProductImageClick = { navigationActions.navigateToProductDetail() },
            onProfileAvatarClick = { navigationActions.navigateToProfile() }
        )
    }

    composable(NavigationDestination.ProductDetail.route) {
        ProductDetailScreen(
            onBackClick = { navigationActions.navigateBack() },
            onContactSellerClick = { /* TODO */ }
        )
    }
}