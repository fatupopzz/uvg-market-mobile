package com.example.uvgmarket.presentation.marketplace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.presentation.components.CustomSearchBar
import com.example.uvgmarket.presentation.components.Restaurant
import com.example.uvgmarket.presentation.components.RestaurantCard
import com.example.uvgmarket.presentation.theme.AppColors

/**
 * Pantalla principal del Marketplace
 * Muestra una lista de restaurantes con barra de búsqueda y FAB
 */
@Composable
fun MarketplaceScreen(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onRestaurantClick: (Restaurant) -> Unit = {},
    onFabClick: () -> Unit = {}
) {
    // Estado para el texto de búsqueda
    var searchText by remember { mutableStateOf("") }

    // Lista de restaurantes hardcodeada para testing
    val restaurantsList = getHardcodedRestaurants()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppColors.BackgroundWhite,
        floatingActionButton = {
            // Floating Action Button verde vacío (puedes agregar tu imagen aquí)
            FloatingActionButton(
                onClick = onFabClick,
                containerColor = AppColors.UvgGreen,
                contentColor = AppColors.TextWhite
            ) {
                // TODO: Agregar tu imagen aquí
                // Ejemplo para cuando tengas tu imagen:
                // Icon(
                //     painter = painterResource(id = R.drawable.tu_icono_chat),
                //     contentDescription = "Abrir chats",
                //     modifier = Modifier.size(24.dp)
                // )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header con fondo verde y barra de búsqueda
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.UvgGreen)
                    .padding(16.dp)
            ) {
                CustomSearchBar(
                    searchText = searchText,
                    onSearchTextChange = { searchText = it },
                    onMenuClick = onMenuClick,
                    onSearchClick = onSearchClick
                )
            }

            // Lista de restaurantes
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                itemsIndexed(
                    items = restaurantsList
                ) { index, restaurant ->
                    RestaurantCard(
                        restaurant = restaurant,
                        onClick = { onRestaurantClick(restaurant) }
                    )
                }
            }
        }
    }
}

/**
 * Función que retorna una lista hardcodeada de restaurantes para testing
 * En el futuro, esto vendrá de un ViewModel o repositorio
 */
private fun getHardcodedRestaurants(): List<Restaurant> {
    return listOf(
        Restaurant(
            name = "Hamburguesas kawaii",
            description = "Tu lugar fav para comer",
            rating = 3,
            profileImageRes = android.R.drawable.ic_menu_camera, // Placeholder
            foodImages = listOf(
                android.R.drawable.ic_menu_gallery,
                android.R.drawable.ic_menu_camera
            )
        ),
        Restaurant(
            name = "Tacos El Sazón",
            description = "Auténticos tacos mexicanos",
            rating = 4,
            profileImageRes = android.R.drawable.ic_menu_camera, // Placeholder
            foodImages = listOf(
                android.R.drawable.ic_menu_gallery,
                android.R.drawable.ic_menu_camera
            )
        ),
        Restaurant(
            name = "Pizza Express",
            description = "Las mejores pizzas de la ciudad",
            rating = 4,
            profileImageRes = android.R.drawable.ic_menu_camera,
            foodImages = listOf(
                android.R.drawable.ic_menu_gallery,
                android.R.drawable.ic_menu_camera
            )
        )
    )
}

/**
 * Preview de la pantalla MarketplaceScreen
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MarketplaceScreenPreview() {
    MarketplaceScreen()
}