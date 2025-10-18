package com.example.uvgmarket.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.uvgmarket.profile.models.Usuario
import com.example.uvgmarket.profile.models.Producto
import com.example.uvgmarket.profile.repository.DummyRepository
import com.example.uvgmarket.profile.components.CustomCoverImage
import com.example.uvgmarket.profile.components.CustomDivider
import com.example.uvgmarket.profile.components.CustomFloatingActionButton
import com.example.uvgmarket.profile.components.CustomProductCard
import com.example.uvgmarket.profile.components.CustomTopBar
import com.example.uvgmarket.profile.components.CustomInfoCard
import com.example.uvgmarket.profile.components.CustomProfileImage
import com.example.uvgmarket.ui.theme.UvgMarketTheme

@Composable
fun ProfileScreen(
    usuario: Usuario,
    productos: List<Producto> = emptyList(),
    onBackClick: () -> Unit = {},
    onChatClick: (String) -> Unit = {},
    onProductoClick: (String) -> Unit = {},
    onFloatingActionClick: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagen de portada
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    CustomCoverImage(
                        imageRes = usuario.imagenPortada,
                        contentDescription = "Portada de ${usuario.nombre}"
                    )

                    CustomTopBar(
                        onBackClick = onBackClick,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            }

            // Separador verde
            item {
                CustomDivider(
                    thickness = 18.dp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            // Información del usuario
            item {
                CustomInfoCard(
                    usuario = usuario,
                    onChatClick = onChatClick
                )
            }

            // Separador después del card del usuario
            item {
                CustomDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Lista de productos
            items(productos) { producto ->
                CustomProductCard(
                    producto = producto,
                    onClick = { onProductoClick(producto.id) }
                )
            }

            // Espacio para el botón flotante
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Foto de perfil superpuesta
        CustomProfileImage(
            imageRes = usuario.imagenPerfil,
            contentDescription = usuario.nombre,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 16.dp, y = 130.dp)
                .zIndex(1f)
        )

        // Botón flotante circular
        CustomFloatingActionButton(
            onClick = onFloatingActionClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilScreenPreview() {
    val repository = DummyRepository()
    UvgMarketTheme {
        ProfileScreen(
            usuario = repository.getUsuario(),
            productos = repository.getProductos(),
            onBackClick = { /* Preview action */ },
            onChatClick = { userId -> /* Preview action */ },
            onProductoClick = { productId -> /* Preview action */ },
            onFloatingActionClick = { /* Preview action */ }
        )
    }
}