package com.example.uvgmarket.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.uvgmarket.profile.models.Usuario
import com.example.uvgmarket.profile.models.Producto
import com.example.uvgmarket.profile.repository.DummyRepository
import com.example.uvgmarket.ui.theme.UvgMarketTheme

@Composable
fun MyProfileScreen(
    usuario: Usuario,
    productos: List<Producto> = emptyList(),
    onBackClick: () -> Unit = {},
    onProductoClick: (String) -> Unit = {},
    onEditClick: () -> Unit = {},
    onAddProductClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ProfileScreen(
        usuario = usuario,
        productos = productos,
        onBackClick = onBackClick,
        onProductoClick = onProductoClick,
        onFloatingActionClick = onAddProductClick,
        onEditClick = onEditClick,
        onChatClick = {},
        showFloatingActionButton = true,
        showChatButton = false,
        showEditButton = true,
        modifier = modifier
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun MyProfileScreenPreview() {
    val repository = DummyRepository()
    UvgMarketTheme {
        MyProfileScreen(
            usuario = repository.getUsuario(),
            productos = repository.getProductos(),
            onBackClick = { /* Preview action */ },
            onProductoClick = { productId -> /* Preview action */ },
            onEditClick = { /* Preview action */ },
            onAddProductClick = { /* Preview action */ }
        )
    }
}