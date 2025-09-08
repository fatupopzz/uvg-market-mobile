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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uvgmarket.profile.models.Usuario
import com.example.uvgmarket.profile.theme.AppColors

@Composable
fun CustomInfoCard(
    usuario: Usuario,
    onChatClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Fila superior: Espacio para foto de perfil y estrellas
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // Estrellas en la parte superior derecha
                CustomStarRating(
                    rating = usuario.calificacion,
                    modifier = Modifier.padding(start = 160.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Contenido debajo de la foto: nombre, descripción
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Nombre del usuario y botón de chat en la misma fila
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = usuario.nombre,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextDark,
                        modifier = Modifier.weight(1f)
                    )

                    CustomChatButton(
                        onClick = { onChatClick(usuario.id) }
                    )
                }

                // Descripción del usuario
                Text(
                    text = usuario.descripcion,
                    fontSize = 16.sp,
                    color = Color(0xFF365236),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}