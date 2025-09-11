package com.example.uvgmarket.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uvgmarket.presentation.welcome.WelcomeBackScreen
import com.example.uvgmarket.presentation.registro.RegistroScreen
import com.example.uvgmarket.pantallainicio.marketplace.MarketplaceScreen
import com.example.uvgmarket.Ordenes_Adr.product_detail.ProductDetailScreen
import com.example.uvgmarket.profile.ProfileScreen
import com.example.uvgmarket.profile.repository.DummyRepository
import com.example.uvgmarket.auth.AuthScreen

// Definir todas las rutas de la aplicación
sealed class MainRoutes(val route: String) {
    object Auth : MainRoutes("auth")                    // Pantalla de selección
    object Login : MainRoutes("login")                  // Login
    object Register : MainRoutes("register")           // Registro
    object Marketplace : MainRoutes("marketplace")     // Pantalla principal
    object ProductDetail : MainRoutes("product_detail") // Detalle producto
    object Profile : MainRoutes("profile")             // Perfil usuario
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val repository = DummyRepository() // Para datos dummy del perfil

    NavHost(
        navController = navController,
        startDestination = MainRoutes.Auth.route // Empezar en pantalla de selección
    ) {

        // 🎭 PANTALLA DE SELECCIÓN AUTH
        composable(MainRoutes.Auth.route) {
            AuthScreen(
                onLogin = {
                    navController.navigate(MainRoutes.Login.route)
                },
                onRegister = {
                    navController.navigate(MainRoutes.Register.route)
                }
            )
        }

        // 🔐 PANTALLA LOGIN
        composable(MainRoutes.Login.route) {
            WelcomeBackScreen(
                onLoginClick = { usuario, contrasena ->
                    // Login exitoso → ir al marketplace
                    navController.navigate(MainRoutes.Marketplace.route) {
                        // Limpiar el stack para que no pueda volver atrás
                        popUpTo(MainRoutes.Auth.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(MainRoutes.Register.route)
                }
            )
        }

        //  PANTALLA REGISTRO
        composable(MainRoutes.Register.route) {
            RegistroScreen(
                onRegistroClick = { nombre, usuario, correo, contrasena ->
                    // Registro exitoso → ir al marketplace
                    navController.navigate(MainRoutes.Marketplace.route) {
                        popUpTo(MainRoutes.Auth.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(MainRoutes.Login.route)
                }
            )
        }

        //  PANTALLA MARKETPLACE (Principal)
        composable(MainRoutes.Marketplace.route) {
            MarketplaceScreen(
                onMenuClick = {
                    // TODO: Abrir drawer/menú lateral
                },
                onSearchClick = {
                    // TODO: Implementar búsqueda
                },
                onEntrepreneurClick = { entrepreneur ->
                    // Navegar al perfil del emprendedor
                    navController.navigate(MainRoutes.Profile.route)
                },
                onFabClick = {
                    // TODO: Crear nuevo producto/emprendimiento
                }
            )
        }

        // PANTALLA DETALLE DE PRODUCTO
        composable(MainRoutes.ProductDetail.route) {
            ProductDetailScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onContactSellerClick = {
                    // TODO: Abrir chat/contacto
                }
            )
        }

        //  PANTALLA PERFIL
        composable(MainRoutes.Profile.route) {
            ProfileScreen(
                usuario = repository.getUsuario(),
                productos = repository.getProductos(),
                onBackClick = {
                    navController.navigateUp()
                },
                onChatClick = { userId ->
                    // TODO: Abrir chat con usuario
                },
                onProductoClick = { productId ->
                    navController.navigate(MainRoutes.ProductDetail.route)
                },
                onFloatingActionClick = {
                    // TODO: Agregar nuevo producto
                }
            )
        }
    }
}