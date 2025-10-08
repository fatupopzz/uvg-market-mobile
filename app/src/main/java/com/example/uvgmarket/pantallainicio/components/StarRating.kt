package com.example.uvgmarket.pantallainicio.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.core.ui.components.rating.StarRatingBar
import com.example.uvgmarket.pantallainicio.theme.AppColors



@Composable
fun StarRating(
    rating: Int,
    modifier: Modifier = Modifier
) {
    StarRatingBar(
        rating = rating,
        modifier = modifier,
        maxStars = UiConstants.MAX_RATING_STARS,
        starSize = UiConstants.STAR_SIZE_SMALL.dp,
        activeColor = AppColors.StarActive,
        inactiveColor = AppColors.StarInactive
    )
}