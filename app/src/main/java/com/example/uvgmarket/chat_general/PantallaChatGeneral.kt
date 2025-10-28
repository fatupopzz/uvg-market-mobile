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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.chat_general.components.ChatCard
import com.example.uvgmarket.chat_general.models.Chat
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.core.ui.components.topbar.AppTopBar
import com.example.uvgmarket.pantallainicio.components.CustomSearchBar
import com.example.uvgmarket.pantallainicio.components.ProfileAvatar
import com.example.uvgmarket.ui.theme.UvgMarketTheme

/**
 * Pantalla de Chat General que muestra la lista de conversaciones
 * Similar a la interfaz de Telegram/WhatsApp
 */
@Composable
fun PantallaChatGeneral(
    onBackClick: () -> Unit = {},
    onChatClick: (String) -> Unit = {},
    onProfileAvatarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    val chatsList = remember { getHardcodedChats() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
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
                    searchText = searchText,
                    onSearchTextChange = { searchText = it },
                    placeholder = "Search",
                    onMenuClick = { /* Handle menu */ },
                    onSearchClick = { /* Handle search */ }
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
                items(chatsList) { chat ->
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

/**
 * Lista hardcodeada de chats para testing
 * En producción vendría de un repositorio/base de datos
 */
private fun getHardcodedChats(): List<Chat> {
    return listOf(
        Chat(
            id = "1",
            nombreContacto = "Hamburguesas kawaii",
            ultimoMensaje = "no se, ahorita te digo.",
            hora = "2:14 PM",
            imagenPerfil = "fotodeperfilhamburger",
            mensajesNoLeidos = 1
        ),
        Chat(
            id = "2",
            nombreContacto = "Grupo de Ux Lab",
            ultimoMensaje = "Fatima hacete shhhh",
            hora = "2:59 PM",
            imagenPerfil = "fotodeperfiljoyeria",
            mensajesNoLeidos = 1
        )
    )
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