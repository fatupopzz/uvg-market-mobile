package com.example.uvgmarket.profile.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.uvgmarket.core.ui.components.topbar.AppTopBar
import com.example.uvgmarket.ui.theme.UvgMarketTheme

@Composable
fun CustomTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppTopBar(
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@Preview
@Composable
fun CustomTopBarPreview() {
    UvgMarketTheme {
        CustomTopBar(onBackClick = {})
    }
}