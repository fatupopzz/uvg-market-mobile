package com.example.uvgmarket.pantallainicio.marketplace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.R
import com.example.uvgmarket.pantallainicio.components.CustomSearchBar
import com.example.uvgmarket.pantallainicio.components.ProfileAvatar
import com.example.uvgmarket.pantallainicio.components.Entrepreneur
import com.example.uvgmarket.pantallainicio.components.EntrepreneurCard
import com.example.uvgmarket.pantallainicio.theme.AppColors

/**
 * Pantalla principal del Marketplace
 * Muestra una lista de emprendedores con barra de búsqueda y FAB
 */
@Composable
fun MarketplaceScreen(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onEntrepreneurClick: (Entrepreneur) -> Unit = {},
    onFabClick: () -> Unit = {},
    onProductImageClick: (String) -> Unit = {},
    onProfileAvatarClick: () -> Unit = {}
) {
    // Estado para el texto de búsqueda
    var searchText by remember { mutableStateOf("") }

    // Lista de emprendedores hardcodeada para testing
    val entrepreneursList = getHardcodedEntrepreneurs()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppColors.BackgroundWhite,
        floatingActionButton = {
            // Floating Action Button verde con ícono de agregar
            FloatingActionButton(
                onClick = onFabClick,
                containerColor = AppColors.UvgGreen,
                contentColor = AppColors.TextWhite
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.chat),
                    contentDescription = "Ver chat"
                )
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
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Barra de búsqueda
                    CustomSearchBar(
                        searchText = searchText,
                        onSearchTextChange = { searchText = it },
                        onMenuClick = onMenuClick,
                        onSearchClick = onSearchClick,
                        placeholder = stringResource(R.string.barra_busqueda_menu),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    ProfileAvatar(
                        size = 30,
                        profileImage = "fotodeperfilindu",
                        onClick = onProfileAvatarClick
                    )
                }
            }

            // Lista de emprendedores
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                itemsIndexed(
                    items = entrepreneursList
                ) { index, entrepreneur ->
                    EntrepreneurCard(
                        entrepreneur = entrepreneur,
                        onClick = { onEntrepreneurClick(entrepreneur) },
                        onProductImageClick = onProductImageClick
                    )
                }
            }
        }
    }
}


private fun getHardcodedEntrepreneurs(): List<Entrepreneur> {
    return listOf(
        Entrepreneur(
            name = "Hamburguesas kawaii",
            description = "Tu lugar fav para comer",
            rating = 3,
            profileImage = "fotodeperfilhamburger",
            productImages = listOf(
                "hamburger1",
                "hamburger2"
            )
        ),
        Entrepreneur(
            name = "Accesorios Luna",
            description = "Joyería artesanal hecha a mano",
            rating = 5,
            profileImage = "fotodeperfiljoyeria",
            productImages = listOf(
                "joyeria1",
                "joyeria2"
            )
        ),
        Entrepreneur(
            name = "TechRepair GT",
            description = "Reparación de celulares y laptops",
            rating = 4,
            profileImage = "imagendeperfilcomputadora",
            productImages = listOf(
                "limpinado1",
                "limpiando2"
            )
        )
    )
}

/**
 * Preview de la pantalla MarketplaceScreen
 */
@Preview(
    showBackground = true,
    showSystemUi = false,
    device = "spec:width=360dp,height=640dp"
)
@Composable
fun MarketplaceScreenPreview() {
    MarketplaceScreen()
}