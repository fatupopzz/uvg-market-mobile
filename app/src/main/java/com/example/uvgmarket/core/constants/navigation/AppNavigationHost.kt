package com.example.uvgmarket.core.constants.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.uvgmarket.core.navigation.NavigationActions
import com.example.uvgmarket.core.navigation.NavigationDestination

@Composable
fun AppNavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navigationActions = remember(navController) {
        NavigationActions(navController)
    }

    NavHost(
        navController = navController,
        startDestination = NavigationDestination.Auth.route,
        modifier = modifier
    ) {
        authGraph(navigationActions)
        marketplaceGraph(navigationActions)
        profileGraph(navigationActions)
    }
}