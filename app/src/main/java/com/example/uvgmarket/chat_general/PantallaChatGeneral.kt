package com.example.uvgmarket.chat_general

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uvgmarket.chat_general.components.ChatCard
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.core.ui.components.topbar.AppTopBar
import com.example.uvgmarket.pantallainicio.components.CustomSearchBar
import com.example.uvgmarket.pantallainicio.components.ProfileAvatar
import com.example.uvgmarket.ui.theme.UvgMarketTheme

/**
 * Pantalla de Chat General que muestra la lista de conversaciones
 */
@Composable
fun PantallaChatGeneral(
    onBackClick: () -> Unit = {},
    onChatClick: (String) -> Unit = {},
    onProfileAvatarClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ChatGeneralViewModel = viewModel()
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
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
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
                    // Top bar con botón de regreso y avatar de perfil
                    ChatGeneralTopBar(
                        onBackClick = onBackClick,
                        onProfileAvatarClick = onProfileAvatarClick
                    )

                    // Barra de búsqueda
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(
                                horizontal = UiConstants.PADDING_MEDIUM.dp,
                                vertical = UiConstants.PADDING_SMALL.dp
                            )
                    ) {
                        CustomSearchBar(
                            searchText = uiState.searchText,
                            onSearchTextChange = { viewModel.onSearchTextChange(it) },
                            placeholder = "Search",
                            onSearchClick = { /* Búsqueda en tiempo real */ }
                        )
                    }

                    // Spacer entre barra de búsqueda y chats
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    )

                    // Lista de chats
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentPadding = PaddingValues(vertical = UiConstants.PADDING_SMALL.dp)
                    ) {
                        items(uiState.chats) { chat ->
                            Column {
                                ChatCard(
                                    chat = chat,
                                    onClick = { onChatClick(chat.id) }
                                )
                                // Spacer entre chats (gris oscuro)
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatGeneralTopBar(
    onBackClick: () -> Unit,
    onProfileAvatarClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(horizontal = UiConstants.PADDING_MEDIUM.dp)
    ) {
        // Botón de regreso alineado a la izquierda
        AppTopBar(
            onBackClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart),
            backgroundColor = MaterialTheme.colorScheme.tertiary
        )

        // Avatar de perfil alineado a la derecha
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(vertical = UiConstants.PADDING_SMALL.dp)
        ) {
            ProfileAvatar(
                size = 40,
                profileImage = "fotodeperfilindu",
                onClick = onProfileAvatarClick
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PantallaChatGeneralPreview() {
    UvgMarketTheme {
        PantallaChatGeneral()
    }
}