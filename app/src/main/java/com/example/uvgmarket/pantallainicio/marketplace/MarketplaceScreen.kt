package com.example.uvgmarket.pantallainicio.marketplace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.R
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.pantallainicio.components.CustomSearchBar
import com.example.uvgmarket.pantallainicio.components.ProfileAvatar
import com.example.uvgmarket.pantallainicio.components.Entrepreneur
import com.example.uvgmarket.pantallainicio.components.EntrepreneurCard
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import com.example.uvgmarket.ui.theme.UvgMarketTheme

/**
 * Pantalla principal del Marketplace.
 * Muestra una lista de emprendedores con barra de búsqueda y FAB.
 */
@Composable
fun MarketplaceScreen(
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit = {},
    onEntrepreneurClick: (Entrepreneur) -> Unit = {},
    onEntrepreneurStarClick: (Entrepreneur) -> Unit = {},
    onFabClick: () -> Unit = {},
    onProductImageClick: (String) -> Unit = {},
    onProfileAvatarClick: () -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }
    val entrepreneursList = remember { getHardcodedEntrepreneurs() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header con fondo verde y barra de búsqueda
            MarketplaceHeader(
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                onSearchClick = onSearchClick,
                onProfileAvatarClick = onProfileAvatarClick
            )

            // Lista de emprendedores
            EntrepreneursList(
                entrepreneurs = entrepreneursList,
                onEntrepreneurClick = onEntrepreneurClick,
                onEntrepreneurStarClick = onEntrepreneurStarClick,
                onProductImageClick = onProductImageClick
            )
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = onFabClick,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(UiConstants.PADDING_MEDIUM.dp)
                .navigationBarsPadding()
        ) {
            Icon(
                modifier = Modifier.size(UiConstants.ICON_SIZE_MEDIUM.dp),
                painter = painterResource(id = R.drawable.chat),
                contentDescription = "Ver chat"
            )
        }
    }
}

@Composable
private fun MarketplaceHeader(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onProfileAvatarClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(
                horizontal = UiConstants.PADDING_MEDIUM.dp,
                vertical = UiConstants.PADDING_MEDIUM.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomSearchBar(
                searchText = searchText,
                onSearchTextChange = onSearchTextChange,
                onSearchClick = onSearchClick,
                placeholder = stringResource(R.string.barra_busqueda_menu),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(UiConstants.PADDING_MEDIUM.dp))

            ProfileAvatar(
                size = 35,
                profileImage = "fotodeperfilindu",
                onClick = onProfileAvatarClick
            )
        }
    }
}

@Composable
private fun EntrepreneursList(
    entrepreneurs: List<Entrepreneur>,
    onEntrepreneurClick: (Entrepreneur) -> Unit,
    onEntrepreneurStarClick: (Entrepreneur) -> Unit,
    onProductImageClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = UiConstants.PADDING_SMALL.dp)
    ) {
        itemsIndexed(
            items = entrepreneurs
        ) { _, entrepreneur ->
            EntrepreneurCard(
                entrepreneur = entrepreneur,
                onClick = { onEntrepreneurClick(entrepreneur) },
                onStarClick = { onEntrepreneurStarClick(entrepreneur) },
                onProductImageClick = onProductImageClick
            )
        }
    }
}

/**
 * Retorna una lista hardcodeada de emprendedores para testing.
 * En producción, esto vendría de un repositorio/API.
 */
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

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun MarketplaceScreenPreview() {
    UvgMarketTheme {
        MarketplaceScreen()
    }
}