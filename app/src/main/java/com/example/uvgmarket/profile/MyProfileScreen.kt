package com.example.uvgmarket.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uvgmarket.ui.theme.UvgMarketTheme

@Composable
fun MyProfileScreen(
    onBackClick: () -> Unit = {},
    onProductoClick: (String) -> Unit = {},
    onEditClick: () -> Unit = {},
    onAddProductClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    // Refrescar cuando regresa a la pantalla
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    ProfileScreen(
        userId = null,
        isOwnProfile = true,
        onBackClick = onBackClick,
        onProductoClick = onProductoClick,
        onFloatingActionClick = onAddProductClick,
        onEditClick = onEditClick,
        onChatClick = {},
        onStarClick = {},
        modifier = modifier,
        viewModel = viewModel
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyProfileScreenPreview() {
    UvgMarketTheme {
        MyProfileScreen()
    }
}