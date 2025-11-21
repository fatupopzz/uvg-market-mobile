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
import com.example.uvgmarket.profile.ProfileViewModel

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
    val uiState by viewModel.uiState.collectAsState()
    var showRatingDialog by remember { mutableStateOf(false) }
    var selectedEntrepreneur by remember { mutableStateOf<Entrepreneur?>(null) }

    // ViewModel para manejar las calificaciones
    val profileViewModel: ProfileViewModel = viewModel()
    val profileUiState by profileViewModel.uiState.collectAsState()

    // NUEVO: Refrescar cuando se cierra el diálogo de calificación y fue exitoso
    LaunchedEffect(showRatingDialog) {
        if (!showRatingDialog && selectedEntrepreneur != null) {
            // Pequeño delay para que Firebase se actualice
            kotlinx.coroutines.delay(1000)
            viewModel.refresh()
            selectedEntrepreneur = null
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

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

            else -> {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MarketplaceHeader(
                        searchText = uiState.searchText,
                        onSearchTextChange = { viewModel.onSearchTextChange(it) },
                        onSearchClick = { viewModel.onSearchClick() },
                        onClearClick = { viewModel.clearSearch() },
                        onProfileAvatarClick = onProfileAvatarClick
                    )

                    if (uiState.isSearching) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = if (uiState.entrepreneurs.isEmpty()) {
                                    "No se encontraron resultados para \"${uiState.searchText}\""
                                } else {
                                    "Mostrando ${uiState.entrepreneurs.size} resultado(s) para \"${uiState.searchText}\""
                                },
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    EntrepreneursList(
                        entrepreneurs = uiState.entrepreneurs,
                        onEntrepreneurClick = onEntrepreneurClick,
                        onEntrepreneurStarClick = { entrepreneur ->
                            selectedEntrepreneur = entrepreneur
                            showRatingDialog = true
                        },
                        onProductImageClick = onProductImageClick
                    )
                }

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

        // Diálogo de calificación
        if (showRatingDialog && selectedEntrepreneur != null) {
            // Cargar el rating actual cuando se abre el diálogo
            LaunchedEffect(selectedEntrepreneur) {
                profileViewModel.loadUserProfile(selectedEntrepreneur!!.id)
            }

            com.example.uvgmarket.core.ui.components.rating.RatingDialog(
                userName = selectedEntrepreneur!!.name,
                currentRating = profileUiState.currentUserRating,
                onDismiss = {
                    showRatingDialog = false
                },
                onRatingSubmit = { newRating ->
                    profileViewModel.rateUser(selectedEntrepreneur!!.id, newRating)
                    showRatingDialog = false
                }
            )
        }
    }
}

@Composable
private fun MarketplaceHeader(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onClearClick: () -> Unit,
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
                onClearClick = onClearClick,
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
    if (entrepreneurs.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "No hay emprendedores para mostrar",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Intenta con otra búsqueda",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
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
                    onProductImageClick = { productId ->
                        onProductImageClick(productId)
                    }
                )
            }
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