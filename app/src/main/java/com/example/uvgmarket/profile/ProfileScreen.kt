package com.example.uvgmarket.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uvgmarket.R
import com.example.uvgmarket.core.ui.components.rating.RatingDialog
import com.example.uvgmarket.data.model.Product
import com.example.uvgmarket.data.model.Seller
import com.example.uvgmarket.profile.components.CustomCoverImage
import com.example.uvgmarket.profile.components.CustomDivider
import com.example.uvgmarket.profile.components.CustomFloatingActionButton
import com.example.uvgmarket.profile.components.CustomTopBar
import com.example.uvgmarket.profile.components.ProfileInfoCard
import com.example.uvgmarket.profile.components.ProfileProductCard
import com.example.uvgmarket.profile.components.CustomProfileImage
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
    val context = LocalContext.current

    // Cargar datos según el tipo de perfil
    LaunchedEffect(userId, isOwnProfile) {
        if (isOwnProfile || userId == null) {
            viewModel.loadCurrentUserProfile()
        } else {
            viewModel.loadUserProfile(userId)
        }
    }

    // Observar el estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Estado para el dialog de calificación
    var showRatingDialog by remember { mutableStateOf(false) }
    var currentRating by remember { mutableStateOf(0) }

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
            uiState.seller != null -> {
                val seller = uiState.seller!!
                val productos = uiState.productos

                // Actualizar rating actual
                LaunchedEffect(seller.calificacion) {
                    currentRating = seller.calificacion.toInt()
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Imagen de portada con TopBar superpuesto
                        item {
                            Box {
                                ProfileCoverImage(
                                    imageName = seller.imagenPortada,
                                    contentDescription = "Portada de ${seller.nombre}"
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
                            ProfileInfoCard(
                                seller = seller,
                                currentRating = currentRating,
                                onChatClick = onChatClick,
                                showChatButton = !isOwnProfile,
                                showEditButton = isOwnProfile,
                                onEditClick = onEditClick,
                                onStarClick = {
                                    if (!isOwnProfile) {
                                        showRatingDialog = true
                                    }
                                }
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
                            ProfileProductCard(
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
                    ProfileAvatarImage(
                        imageName = seller.imagenPerfil,
                        contentDescription = seller.nombre,
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

                    // Dialog de calificación
                    if (showRatingDialog) {
                        RatingDialog(
                            userName = seller.nombre,
                            currentRating = currentRating,
                            onDismiss = { showRatingDialog = false },
                            onRatingSubmit = { newRating ->
                                currentRating = newRating
                                // TODO: Guardar en Firebase
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileCoverImage(
    imageName: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(
        imageName,
        "drawable",
        context.packageName
    )

    Image(
        painter = painterResource(
            id = if (imageResId != 0) imageResId else R.drawable.portada_perfil
        ),
        contentDescription = contentDescription,
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun ProfileAvatarImage(
    imageName: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(
        imageName,
        "drawable",
        context.packageName
    )

    CustomProfileImage(
        imageRes = if (imageResId != 0) imageResId else R.drawable.profile_picture,
        contentDescription = contentDescription,
        modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    UvgMarketTheme {
        ProfileScreen(isOwnProfile = true)
    }
}