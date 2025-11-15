package com.example.uvgmarket.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uvgmarket.profile.components.*
import com.example.uvgmarket.profile.models.Usuario
import com.example.uvgmarket.profile.models.Producto
import com.example.uvgmarket.ui.theme.UvgMarketTheme

/**
 * Pantalla de perfil que muestra información del usuario y sus productos
 * Puede ser el perfil propio o el de otro usuario
 */
@Composable
fun ProfileScreen(
    userId: String? = null,
    isOwnProfile: Boolean = true,
    onBackClick: () -> Unit = {},
    onChatClick: (String) -> Unit = {},
    onProductoClick: (String) -> Unit = {},
    onFloatingActionClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onStarClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Cargar datos al iniciar la pantalla
    LaunchedEffect(userId) {
        if (userId != null) {
            viewModel.loadUserProfile(userId)
        } else {
            viewModel.loadCurrentUserProfile()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        when {
            // Estado de carga
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Estado de error
            uiState.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = uiState.error ?: "Error desconocido",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Estado con datos
            uiState.usuario != null -> {
                val usuario = uiState.usuario!!
                val productos = uiState.productos

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
                                onChatClick = onChatClick,
                                showChatButton = !isOwnProfile,
                                showEditButton = isOwnProfile,
                                onEditClick = onEditClick,
                                onStarClick = onStarClick
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
                                onClick = { onProductoClick(producto.id) },
                                showDeleteButton = isOwnProfile,
                                onDeleteClick = { viewModel.deleteProduct(producto.id) }
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

                    // Botón flotante circular solo si es perfil propio
                    if (isOwnProfile) {
                        CustomFloatingActionButton(
                            onClick = onFloatingActionClick,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    UvgMarketTheme {
        ProfileScreen(isOwnProfile = true)
    }
}