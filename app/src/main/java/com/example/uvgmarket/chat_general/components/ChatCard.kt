package com.example.uvgmarket.chat_general.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uvgmarket.chat_general.models.Chat
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.ui.theme.UvgMarketTheme

/**
 * Componente que muestra un elemento individual de chat en la lista
 * Incluye avatar, nombre, último mensaje, hora y badge de mensajes no leídos
 */
@Composable
fun ChatCard(
    chat: Chat,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(
                horizontal = UiConstants.PADDING_MEDIUM.dp,
                vertical = UiConstants.PADDING_SMALL.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Avatar del contacto
        Box(
            modifier = Modifier
                .size(UiConstants.AVATAR_SIZE_MEDIUM.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            // Obtener el resource ID dinámicamente
            val imageResId = context.resources.getIdentifier(
                chat.imagenPerfil,
                "drawable",
                context.packageName
            )

            if (imageResId != 0) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "Avatar de ${chat.nombreContacto}",
                    modifier = Modifier
                        .size(UiConstants.AVATAR_SIZE_MEDIUM.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Fallback al ícono por defecto
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar de ${chat.nombreContacto}",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(UiConstants.ICON_SIZE_MEDIUM.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(UiConstants.PADDING_MEDIUM.dp))

        // Información del chat (nombre y último mensaje)
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = chat.nombreContacto,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = chat.ultimoMensaje,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.width(UiConstants.PADDING_SMALL.dp))

        // Hora y badge de mensajes no leídos
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Hora
            Text(
                text = chat.hora,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Normal
            )

            // Badge de mensajes no leídos
            if (chat.mensajesNoLeidos > 0) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.error),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = chat.mensajesNoLeidos.toString(),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onError,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatCardPreview() {
    UvgMarketTheme {
        ChatCard(
            chat = Chat(
                id = "1",
                nombreContacto = "Hamburguesas kawaii",
                ultimoMensaje = "no se, ahorita te digo.",
                hora = "2:14 PM",
                imagenPerfil = "hamburger1",
                mensajesNoLeidos = 1
            ),
            onClick = {}
        )
    }
}