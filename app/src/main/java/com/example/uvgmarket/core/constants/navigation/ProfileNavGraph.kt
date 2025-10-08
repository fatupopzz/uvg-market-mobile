package com.example.uvgmarket.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.uvgmarket.profile.ProfileScreen
import com.example.uvgmarket.profile.repository.DummyRepository

fun NavGraphBuilder.profileGraph(navigationActions: NavigationActions) {
    composable(NavigationDestination.Profile.route) {
        val repository = DummyRepository()

        ProfileScreen(
            usuario = repository.getUsuario(),
            productos = repository.getProductos(),
            onBackClick = { navigationActions.navigateBack() },
            onChatClick = { /* TODO */ },
            onProductoClick = { navigationActions.navigateToProductDetail() },
            onFloatingActionClick = { /* TODO */ }
        )
    }
}