package com.example.uvgmarket.profile.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.core.ui.components.images.CircularImage

@Composable
fun CustomProfileImage(
    imageRes: Int,
    contentDescription: String,
    size: Dp = 140.dp,
    modifier: Modifier = Modifier
) {
    CircularImage(
        imageRes = imageRes,
        contentDescription = contentDescription,
        size = size,
        modifier = modifier
    )
}