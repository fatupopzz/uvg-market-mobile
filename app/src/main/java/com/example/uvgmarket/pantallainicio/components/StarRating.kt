package com.example.uvgmarket.pantallainicio.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.core.ui.components.rating.StarRatingBar
import com.example.uvgmarket.ui.theme.UvgMarketTheme


@Composable
fun StarRating(
    rating: Int,
    modifier: Modifier = Modifier
) {
    StarRatingBar(
        rating = rating,
        modifier = modifier,
    )
}

@Preview
@Composable
fun StarRatingPreview() {
    UvgMarketTheme {
        StarRating(rating = 3)
    }
}