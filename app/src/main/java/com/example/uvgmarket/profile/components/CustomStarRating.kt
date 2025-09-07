package com.example.uvgmarket.profile.components

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
import com.example.uvgmarket.profile.theme.AppColors

@Composable
fun CustomStarRating(
    rating: Float,
    maxStars: Int = 5,
    starSize: Dp = 36.dp,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(maxStars) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (index < rating) AppColors.UvgGreen else Color.Gray,
                modifier = Modifier.size(starSize)
            )
        }
    }
}
