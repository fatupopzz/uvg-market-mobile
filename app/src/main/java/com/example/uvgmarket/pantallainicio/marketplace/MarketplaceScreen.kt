package com.example.uvgmarket.pantallainicio.marketplace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uvgmarket.R
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.pantallainicio.components.CustomSearchBar
import com.example.uvgmarket.pantallainicio.components.ProfileAvatar
import com.example.uvgmarket.pantallainicio.components.Entrepreneur
import com.example.uvgmarket.pantallainicio.components.EntrepreneurCard
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
    onProfileAvatarClick: () -> Unit = {},
    viewModel: MarketplaceViewModel = viewModel()
) {
    // Observar el estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        when {
            // Estado de carga
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Estado de error
            uiState.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = uiState.error ?: "Error desconocido",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Estado con datos
            else -> {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header con fondo verde y barra de búsqueda
                    MarketplaceHeader(
                        searchText = uiState.searchText,
                        onSearchTextChange = { viewModel.onSearchTextChange(it) },
                        onSearchClick = { viewModel.onSearchClick() },
                        onProfileAvatarClick = onProfileAvatarClick
                    )

                    // Lista de emprendedores
                    EntrepreneursList(
                        entrepreneurs = uiState.entrepreneurs,
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