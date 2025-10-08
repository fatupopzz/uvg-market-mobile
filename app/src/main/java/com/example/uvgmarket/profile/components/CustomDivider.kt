package com.example.uvgmarket.profile.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.core.ui.components.dividers.AppDivider

@Composable
fun CustomDivider(
    thickness: Dp = 1.dp,
    color: Color = Color(0xFF4CAF50).copy(alpha = 0.3f),
    modifier: Modifier = Modifier
) {
    AppDivider(
        modifier = modifier,
        thickness = thickness,
        color = color
    )
}