package com.example.uvgmarket.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomProfileImage(
    imageRes: Int,
    contentDescription: String,
    size: Dp = 140.dp,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = contentDescription,
        modifier = modifier
            .size(size)
            .clip(CircleShape),
            //.border(4.dp, Color.White, CircleShape),
        contentScale = ContentScale.Crop
    )
}