package com.example.uvgmarket.core.ui.components.rating

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.core.constants.UiConstants

@Composable
fun StarRatingBar(
    rating: Int,
    modifier: Modifier = Modifier,
    maxStars: Int = UiConstants.MAX_RATING_STARS,
    starSize: Dp = UiConstants.STAR_SIZE_SMALL.dp,
    activeColor: Color,
    inactiveColor: Color
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(maxStars) { index ->
            val isActive = index < rating

            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = if (isActive) {
                    "Estrella activa"
                } else {
                    "Estrella inactiva"
                },
                tint = if (isActive) activeColor else inactiveColor,
                modifier = Modifier.size(starSize)
            )
        }
    }
}