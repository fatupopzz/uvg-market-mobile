package com.example.uvgmarket.profile

import androidx.compose.runtime.Composable
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
    ProfileScreen(
        userId = null, // null indica que es el perfil propio
        isOwnProfile = true,
        onBackClick = onBackClick,
        onProductoClick = onProductoClick,
        onFloatingActionClick = onAddProductClick,
        onEditClick = onEditClick,
        onChatClick = {}, // No hay chat en perfil propio
        onStarClick = {}, // Las estrellas no son clickeables en perfil propio
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