package com.example.uvgmarket.ordenes_Adr.product_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uvgmarket.R
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.core.ui.components.buttons.SecondaryButton
import com.example.uvgmarket.core.ui.components.topbar.AppTopBar
import com.example.uvgmarket.ui.theme.UvgMarketTheme

/**
 * Pantalla de detalle de producto.
 * Muestra información completa del producto seleccionado.
 *
 * @param productId ID del producto a mostrar
 * @param showContactButton Si es true, muestra el botón de contactar vendedor
 */
@Composable
fun ProductDetailScreen(
    productId: String,
    onBackClick: () -> Unit = {},
    onContactSellerClick: () -> Unit = {},
    showContactButton: Boolean = true,
    viewModel: ProductDetailViewModel = viewModel()
) {
    val context = LocalContext.current

    // Cargar producto cuando cambia el ID
    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    // Observar el estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top bar con botón de regreso
            AppTopBar(onBackClick = onBackClick)

            when {
                // Estado de carga
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                // Estado de error
                uiState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.error ?: "Error desconocido",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                // Estado con datos
                uiState.product != null -> {
                    val product = uiState.product!!

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Imagen del producto
                        ProductImage(
                            imageName = product.imagen,
                            contentDescription = product.nombre,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )

                        // Información del producto
                        ProductInformation(
                            title = product.nombre,
                            subtitle = product.subtitulo,
                            description = product.descripcion,
                            price = product.precio,
                            onContactSellerClick = onContactSellerClick,
                            showContactButton = showContactButton
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductImage(
    imageName: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        // Obtener el resource ID dinámicamente
        val imageResId = context.resources.getIdentifier(
            imageName,
            "drawable",
            context.packageName
        )

        if (imageResId != 0) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            // Fallback si no se encuentra la imagen
            Image(
                painter = painterResource(id = R.drawable.product_placeholder),
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun ProductInformation(
    title: String,
    subtitle: String,
    description: String,
    price: Double,
    onContactSellerClick: () -> Unit,
    showContactButton: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(
                horizontal = UiConstants.PADDING_LARGE.dp,
                vertical = UiConstants.PADDING_EXTRA_LARGE.dp
            )
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.Start
    ) {
        // Título del producto
        ProductTitle(text = title)

        Spacer(modifier = Modifier.height(UiConstants.PADDING_SMALL.dp))

        // Subtítulo
        ProductSubtitle(text = subtitle)

        Spacer(modifier = Modifier.height(UiConstants.PADDING_LARGE.dp))

        // Descripción
        ProductDescription(text = description)

        Spacer(modifier = Modifier.height(UiConstants.PADDING_EXTRA_LARGE.dp))

        // Precio
        ProductPrice(price = price)

        Spacer(modifier = Modifier.height(40.dp))

        // Botón de contactar vendedor
        if (showContactButton) {
            SecondaryButton(
                text = stringResource(R.string.boton_contactar_vendedor),
                onClick = onContactSellerClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ProductTitle(text: String) {
    Text(
        text = text,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Left,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ProductSubtitle(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Left,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ProductDescription(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Left,
        lineHeight = 22.sp,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ProductPrice(price: Double) {
    Text(
        text = "Q${String.format("%.2f", price)}",
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Left,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    UvgMarketTheme {
        ProductDetailScreen(productId = "hamburger1")
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenWithoutContactButtonPreview() {
    UvgMarketTheme {
        ProductDetailScreen(
            productId = "joyeria1",
            showContactButton = false
        )
    }
}