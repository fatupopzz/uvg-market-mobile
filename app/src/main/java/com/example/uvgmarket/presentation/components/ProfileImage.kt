package com.example.uvgmarket.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.R

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.avatar_ingresar_datos),
        contentDescription = "Profile Avatar",
        modifier = modifier.clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}