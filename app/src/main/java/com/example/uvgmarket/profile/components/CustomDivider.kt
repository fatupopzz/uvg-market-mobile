package com.example.uvgmarket.profile.components

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomDivider(
    thickness: Dp = 1.dp,
    color: Color = Color(0xFF4CAF50).copy(alpha = 0.3f),
    modifier: Modifier = Modifier
) {
    Divider(
        modifier = modifier.fillMaxWidth(),
        thickness = thickness,
        color = color
    )
}