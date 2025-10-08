package com.example.uvgmarket.profile.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.core.ui.components.rating.StarRatingBar
import com.example.uvgmarket.profile.theme.AppColors

@Composable
fun CustomStarRating(
    rating: Float,
    maxStars: Int = UiConstants.MAX_RATING_STARS,
    starSize: Dp = UiConstants.STAR_SIZE_LARGE.dp,
    modifier: Modifier = Modifier
) {
    StarRatingBar(
        rating = rating.toInt(),
        modifier = modifier,
        maxStars = maxStars,
        starSize = starSize,
        activeColor = AppColors.UvgGreen,
        inactiveColor = Color.Gray
    )
}