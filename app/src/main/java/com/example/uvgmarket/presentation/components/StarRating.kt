package com.example.uvgmarket.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.presentation.theme.AppColors

/**
 * Componente reutilizable para mostrar rating con estrellas
 *
 * @param rating Número de estrellas activas (0-5)
 * @param maxStars Número máximo de estrellas a mostrar (default: 5)
 * @param starSize Tamaño de cada estrella en dp (default: 16dp)
 * @param activeColor Color de las estrellas activas
 * @param inactiveColor Color de las estrellas inactivas
 */
@Composable
fun StarRating(
    rating: Int,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    starSize: Int = 16,
    activeColor: Color = AppColors.StarActive,
    inactiveColor: Color = AppColors.StarInactive
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Iteramos sobre el número máximo de estrellas
        repeat(maxStars) { index ->
            val isActive = index < rating

            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = if (isActive) "Estrella activa" else "Estrella inactiva",
                tint = if (isActive) activeColor else inactiveColor,
                modifier = Modifier.size(starSize.dp)
            )
        }
    }
}

/**
 * Preview del componente StarRating con diferentes configuraciones
 */
@Preview(showBackground = true)
@Composable
fun StarRatingPreview() {
    StarRating(rating = 3)
}