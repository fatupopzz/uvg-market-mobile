package com.example.uvgmarket.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uvgmarket.ui.theme.UvgMarketTheme

/**
 * Componente de burbuja de mensaje que sigue el diseño del mockup
 * Los mensajes enviados aparecen a la derecha con fondo verde claro
 * Los mensajes recibidos aparecen a la izquierda con fondo verde oscuro
 */
@Composable
fun MessageBubble(
    message: Message,
    isCurrentUser: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = if (isCurrentUser) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        Color(0xFF2E5C3E) // Verde oscuro para mensajes recibidos
                    },
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isCurrentUser) 16.dp else 2.dp,
                        bottomEnd = if (isCurrentUser) 2.dp else 16.dp
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = message.text,
                color = if (isCurrentUser) Color.Black else Color.White,
                fontSize = 15.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageBubblePreview() {
    UvgMarketTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE5E5E5))
                .padding(8.dp)
        ) {
            MessageBubble(
                message = Message(
                    text = "Hola, ¿cómo estás?",
                    senderId = "user1"
                ),
                isCurrentUser = false
            )

            MessageBubble(
                message = Message(
                    text = "¡Muy bien! ¿Y tú?",
                    senderId = "currentUser"
                ),
                isCurrentUser = true
            )

            MessageBubble(
                message = Message(
                    text = "También bien, gracias por preguntar",
                    senderId = "user1"
                ),
                isCurrentUser = false
            )
        }
    }
}