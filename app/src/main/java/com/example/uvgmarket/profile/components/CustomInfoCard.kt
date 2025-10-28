package com.example.uvgmarket.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uvgmarket.profile.models.Usuario
import com.example.uvgmarket.profile.repository.DummyRepository
import com.example.uvgmarket.ui.theme.UvgMarketTheme

@Composable
fun CustomInfoCard(
    usuario: Usuario,
    onChatClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    showChatButton: Boolean = true,
    showEditButton: Boolean = false,
    onEditClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                CustomStarRating(
                    rating = usuario.calificacion,
                    modifier = Modifier.padding(start = 160.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = usuario.nombre,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    // Mostrar botón de chat solo si showChatButton es true
                    if (showChatButton) {
                        CustomChatButton(
                            onClick = { onChatClick(usuario.id) }
                        )
                    }

                    // Mostrar botón de editar solo si showEditButton es true
                    if (showEditButton) {
                        CustomEditButton(
                            onClick = onEditClick
                        )
                    }
                }

                Text(
                    text = usuario.descripcion,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun CustomInfoCardPreview() {
    UvgMarketTheme {
        val repository = DummyRepository()
        val usuario = repository.getUsuario()
        CustomInfoCard(
            usuario = usuario,
            onChatClick = { /* Preview action */ }
        )
    }
}

@Preview
@Composable
fun CustomInfoCardWithEditPreview() {
    UvgMarketTheme {
        val repository = DummyRepository()
        val usuario = repository.getUsuario()
        CustomInfoCard(
            usuario = usuario,
            onChatClick = { /* Preview action */ },
            showChatButton = false,
            showEditButton = true,
            onEditClick = { /* Preview action */ }
        )
    }
}