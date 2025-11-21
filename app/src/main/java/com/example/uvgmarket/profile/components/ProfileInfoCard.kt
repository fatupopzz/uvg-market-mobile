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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uvgmarket.data.model.Seller
import com.example.uvgmarket.ui.theme.UvgMarketTheme

@Composable
fun ProfileInfoCard(
    seller: Seller,
    currentRating: Int,
    onChatClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    showChatButton: Boolean = true,
    showEditButton: Boolean = false,
    onEditClick: () -> Unit = {},
    onStarClick: () -> Unit = {}
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
                // Las estrellas son clickeables solo cuando showChatButton es true
                CustomStarRating(
                    rating = currentRating.toFloat(),
                    modifier = Modifier.padding(start = 160.dp),
                    isClickable = showChatButton,
                    onClick = onStarClick
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
                        text = seller.nombre,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    // Mostrar botón de chat solo si showChatButton es true
                    if (showChatButton) {
                        CustomChatButton(
                            onClick = { onChatClick(seller.id) }
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
                    text = seller.descripcion,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
 //Preview
@Preview
@Composable
fun ProfileInfoCardPreview() {
    UvgMarketTheme {
        val seller = Seller(
            id = "1",
            nombre = "Hamburguesas Kawaii",
            descripcion = "Tu lugar favorito para comer",
            imagenPerfil = "profile_picture",
            imagenPortada = "portada_perfil",
            calificacion = 3f
        )
        ProfileInfoCard(
            seller = seller,
            currentRating = 3,
            onChatClick = {}
        )
    }
}

@Preview
@Composable
fun ProfileInfoCardWithEditPreview() {
    UvgMarketTheme {
        val seller = Seller(
            id = "1",
            nombre = "Hamburguesas Kawaii",
            descripcion = "Tu lugar favorito para comer",
            imagenPerfil = "profile_picture",
            imagenPortada = "portada_perfil",
            calificacion = 3f
        )
        ProfileInfoCard(
            seller = seller,
            currentRating = 3,
            onChatClick = {},
            showChatButton = false,
            showEditButton = true,
            onEditClick = {}
        )
    }
}