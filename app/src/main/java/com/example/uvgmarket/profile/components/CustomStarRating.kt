package com.example.uvgmarket.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.core.ui.components.rating.StarRatingBar
import com.example.uvgmarket.ui.theme.UvgMarketTheme

@Composable
fun CustomStarRating(
    rating: Float,
    maxStars: Int = UiConstants.MAX_RATING_STARS,
    starSize: Dp = UiConstants.STAR_SIZE_LARGE.dp,
    modifier: Modifier = Modifier,
    isClickable: Boolean = false,
    onClick: () -> Unit = {}
) {
    val finalModifier = if (isClickable) {
        modifier.clickable { onClick() }
    } else {
        modifier
    }

    StarRatingBar(
        rating = rating.toInt(),
        modifier = finalModifier,
        maxStars = maxStars,
        starSize = starSize,
    )
}

@Preview
@Composable
fun CustomStarRatingPreview() {
    UvgMarketTheme {
        CustomStarRating(rating = 3.0f)
    }
}

@Preview
@Composable
fun CustomStarRatingClickablePreview() {
    UvgMarketTheme {
        CustomStarRating(
            rating = 3.0f,
            isClickable = true,
            onClick = { /* Click action */ }
        )
    }
}