package com.example.uvgmarket.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uvgmarket.R
import com.example.uvgmarket.core.ui.components.rating.RatingDialog
import com.example.uvgmarket.data.model.Product
import com.example.uvgmarket.profile.components.CustomCoverImage
import com.example.uvgmarket.profile.components.CustomDivider
import com.example.uvgmarket.profile.components.CustomFloatingActionButton
import com.example.uvgmarket.profile.components.CustomTopBar
import com.example.uvgmarket.profile.components.ProfileInfoCard
import com.example.uvgmarket.profile.components.ProfileProductCard
import com.example.uvgmarket.profile.components.CustomProfileImage
import com.example.uvgmarket.ui.theme.UvgMarketTheme

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

    LaunchedEffect(userId, isOwnProfile) {
        if (isOwnProfile || userId == null) {
            viewModel.loadCurrentUserProfile()
        } else {
            viewModel.loadUserProfile(userId)
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

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

            uiState.seller != null -> {
                val seller = uiState.seller!!
                val productos = uiState.productos

                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
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

                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset(y = (-70).dp)
                                    .padding(start = 16.dp)
                            ) {
                                ProfileAvatarImage(
                                    imageName = seller.imagenPerfil,
                                    contentDescription = seller.nombre,
                                    modifier = Modifier.align(Alignment.CenterStart)
                                )
                            }
                        }

                        item {
                            CustomDivider(
                                thickness = 18.dp,
                                color = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.offset(y = (-70).dp)
                            )
                        }

                        item {
                            ProfileInfoCard(
                                seller = seller,
                                currentRating = uiState.currentUserRating,
                                onChatClick = onChatClick,
                                showChatButton = !isOwnProfile,
                                showEditButton = isOwnProfile,
                                onEditClick = onEditClick,
                                onStarClick = {
                                    if (!isOwnProfile) {
                                        viewModel.showRatingDialog()
                                    }
                                },
                                modifier = Modifier.offset(y = (-70).dp)
                            )
                        }

                        item {
                            CustomDivider(
                                thickness = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.offset(y = (-70).dp)
                            )
                        }

                        items(productos) { producto ->
                            ProfileProductCard(
                                producto = producto,
                                onClick = { onProductoClick(producto.id) },
                                showDeleteButton = isOwnProfile,
                                onDeleteClick = {
                                    viewModel.showDeleteDialog(producto)
                                },
                                modifier = Modifier.offset(y = (-70).dp)
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }

                    if (isOwnProfile) {
                        CustomFloatingActionButton(
                            onClick = onFloatingActionClick,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp)
                                .navigationBarsPadding()
                        )
                    }

                    // Diálogo de calificación
                    if (uiState.showRatingDialog) {
                        RatingDialog(
                            userName = seller.nombre,
                            currentRating = uiState.currentUserRating,
                            onDismiss = { viewModel.hideRatingDialog() },
                            onRatingSubmit = { newRating ->
                                viewModel.rateUser(seller.id, newRating)
                            }
                        )
                    }

                    // Diálogo de confirmación de eliminación
                    if (uiState.showDeleteDialog && uiState.productToDelete != null) {
                        DeleteProductDialog(
                            productName = uiState.productToDelete!!.nombre,
                            onConfirm = { viewModel.confirmDeleteProduct() },
                            onDismiss = { viewModel.hideDeleteDialog() }
                        )
                    }

                    // Snackbar de éxito
                    if (uiState.deleteSuccess != null) {
                        Snackbar(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp)
                                .navigationBarsPadding(),
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = uiState.deleteSuccess ?: "",
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DeleteProductDialog(
    productName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Eliminar Producto",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "¿Estás seguro de que deseas eliminar \"$productName\"?\n\nEsta acción no se puede deshacer.",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
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