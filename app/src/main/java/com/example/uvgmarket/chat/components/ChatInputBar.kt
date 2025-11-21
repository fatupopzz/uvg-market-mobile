package com.example.uvgmarket.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uvgmarket.ui.theme.UvgMarketTheme

/**
 * Barra de entrada de mensajes siguiendo el diseño del mockup
 * Incluye campo de texto y botón de envío
 */
@Composable
fun ChatInputBar(
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Campo de texto
        Box(
            modifier = Modifier
                .weight(1f)
                .background(
                    color = if (enabled) {
                        MaterialTheme.colorScheme.surface
                    } else {
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                    },
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            BasicTextField(
                value = messageText,
                onValueChange = { newValue ->
                    if (enabled) {
                        onMessageTextChange(newValue)
                    }
                },
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 15.sp
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if (messageText.isEmpty()) {
                        Text(
                            text = "Message",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 15.sp
                        )
                    }
                    innerTextField()
                }
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Botón de envío
        IconButton(
            onClick = {
                if (messageText.isNotBlank() && enabled) {
                    onSendClick()
                }
            },
            modifier = Modifier.size(40.dp),
            enabled = enabled && messageText.isNotBlank()
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Enviar mensaje",
                tint = if (enabled && messageText.isNotBlank()) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatInputBarPreview() {
    UvgMarketTheme {
        var messageText by remember { mutableStateOf("") }

        ChatInputBar(
            messageText = messageText,
            onMessageTextChange = { messageText = it },
            onSendClick = { messageText = "" }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatInputBarDisabledPreview() {
    UvgMarketTheme {
        var messageText by remember { mutableStateOf("") }

        ChatInputBar(
            messageText = messageText,
            onMessageTextChange = { messageText = it },
            onSendClick = { messageText = "" },
            enabled = false
        )
    }
}