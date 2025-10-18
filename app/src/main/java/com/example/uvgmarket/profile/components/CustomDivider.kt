package com.example.uvgmarket.profile.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.core.ui.components.dividers.AppDivider
import com.example.uvgmarket.ui.theme.UvgMarketTheme

@Composable
fun CustomDivider(
    thickness: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
    modifier: Modifier = Modifier
) {
    AppDivider(
        modifier = modifier,
        thickness = thickness,
        color = color
    )
}

@Preview
@Composable
fun CustomDividerPreview() {
    UvgMarketTheme {
        CustomDivider()
    }
}