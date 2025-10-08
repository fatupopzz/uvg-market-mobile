package com.example.uvgmarket.profile.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.uvgmarket.core.ui.components.topbar.AppTopBar
import com.example.uvgmarket.profile.theme.AppColors

@Composable
fun CustomTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppTopBar(
        onBackClick = onBackClick,
        modifier = modifier,
        backgroundColor = AppColors.UvgGreenLight.copy(alpha = 0.8f)
    )
}