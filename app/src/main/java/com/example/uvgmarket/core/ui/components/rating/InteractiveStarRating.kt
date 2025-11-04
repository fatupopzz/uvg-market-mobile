package com.example.uvgmarket.core.ui.components.rating

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.ui.theme.UvgMarketTheme

/**
 * Componente de calificación con estrellas interactivas
 * Permite al usuario seleccionar una calificación de 1 a 5 estrellas
 *
 * @param rating Calificación actual (0-5)
 * @param onRatingChanged Callback cuando el usuario cambia la calificación
 * @param maxStars Número máximo de estrellas (por defecto 5)
 * @param starSize Tamaño de cada estrella
 * @param activeColor Color de las estrellas activas
 * @param inactiveColor Color de las estrellas inactivas
 */
@Composable
fun InteractiveStarRating(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxStars: Int = UiConstants.MAX_RATING_STARS,
    starSize: Dp = UiConstants.STAR_SIZE_LARGE.dp,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(maxStars) { index ->
            val starIndex = index + 1
            val isActive = starIndex <= rating

            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Calificar con $starIndex ${if (starIndex == 1) "estrella" else "estrellas"}",
                tint = if (isActive) activeColor else inactiveColor,
                modifier = Modifier
                    .size(starSize)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onRatingChanged(starIndex)
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InteractiveStarRatingPreview() {
    UvgMarketTheme {
        var rating by remember { mutableStateOf(3) }

        InteractiveStarRating(
            rating = rating,
            onRatingChanged = { rating = it }
        )
    }
}